package testing.service;

import dk.statsbiblioteket.digitaltv.access.model.*;
import dk.statsbiblioteket.digitaltv.domspreingest.preingester.StringListStringPair;
import dk.statsbiblioteket.generic.utils.FaultException;
import dk.statsbiblioteket.util.Files;
import org.slf4j.LoggerFactory;
import testing.GlobalData;
import testing.model.Program;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created with IntelliJ IDEA.
 * User: asj
 * Date: 29-08-12
 * Time: 13:07
 * To change this template use File | Settings | File Templates.
 */
public class PBCoreGenerator {
    private final org.slf4j.Logger log = LoggerFactory.getLogger(getClass());
    private static ProgramAdapter programAdapter = new ProgramAdapter();
    enum MediaType {
        UNKNOWN, VIDEO, AUDIO, SEVERAL
    }

    public String generateXmlFromTemplate(Program program){
        if (program == null) {
            log.debug("Entered method generateFileToHotfolderFromTemplate"
                    + " with a null ritzauProgram!");
            return null;
        }
        log.debug("Entered method generateXmlFromTemplate('"
                + "Prgrogram{" + program.getTitle() + "}')");
        File templateFile;
        String templateWorkingCopy;

        templateFile = new File(GlobalData.getPathToTemplate());

        // Read template
        try {
            templateWorkingCopy = Files.loadString(templateFile);
        } catch (IOException e) {
            throw new FaultException("Could not read template file", e);
        }

        // Fill out template
        templateWorkingCopy = replaceEnclosedInCdata(templateWorkingCopy,
                "[INSERT_PBC_ID]",
                Integer.toString(program.getId()));
        // Though the metadata will be a mix of ritzau and gallup data,
        // the gallup data were found through a compositeprogram found
        // from a ritzau id, so we let the ritzau id be the id of the
        // ritzauProgram.
        templateWorkingCopy = replaceEnclosedInCdata(templateWorkingCopy,
                "[INSERT_PBC_ID_SOURCE]",
                "id");

        templateWorkingCopy = replaceEnclosedInCdata(templateWorkingCopy,
                "[INSERT_PBC_SERIE_ID]",
                program.getExtra());

        templateWorkingCopy = replaceEnclosedInCdata(templateWorkingCopy,
                "[INSERT_PBC_MAIN_GENRE_ID]",
                program.getCategory());
        templateWorkingCopy = replaceEnclosedInCdata(templateWorkingCopy,
                "[INSERT_PBC_CHANNEL_ID]",
                program.getChannelName());

        // From query:
        //   digitaltv=> select titel, maintitle from compositeprogram,
        //   ritzauprogram, tvmeterprogram
        //   where ritzauprogram_id=ritzauprogram.id
        //   and tvmeterprogram_originalentry=originalentry;
        // you can see that Ritzau "titel" is usually more informative
        // (and more consistently formatted) than Gallup TVMeter "maintitle".
        templateWorkingCopy = replaceEnclosedInCdata(templateWorkingCopy,
                "[INSERT_PBC_TITLE]",
                program.getTitle());
        templateWorkingCopy = replaceEnclosedInCdata(templateWorkingCopy,
                "[INSERT_PBC_TITLE_TYPE]",
                "titel");

        // Ritzau descriptions are more informative that Gallup TVMeter desc's
        templateWorkingCopy = replaceEnclosedInCdata(templateWorkingCopy,
                "[INSERT_PBC_SHORT_DESCRIPTION]",
                program.getReduceddescription());
        templateWorkingCopy = replaceEnclosedInCdata(templateWorkingCopy,
                "[INSERT_PBC_DESCRIPTION_SHORT]",
                "kortomtale");
        templateWorkingCopy = replaceEnclosedInCdata(templateWorkingCopy,
                "[INSERT_PBC_LONG_DESCRIPTION_1]",
                program.getDescription());
        templateWorkingCopy = replaceEnclosedInCdata(templateWorkingCopy,
                "[INSERT_PBC_DESCRIPTION_LONG_1]",
                "langomtale1");

        templateWorkingCopy = replaceEnclosedInCdata(templateWorkingCopy,
                "[INSERT_PBC_MAIN_GENRE]",
                program.getCategory());
        templateWorkingCopy = replaceEnclosedInCdata(templateWorkingCopy,
                "[INSERT_PBC_TVMETER_CONTENTTYPE]",
                "");

        templateWorkingCopy = replaceEnclosedInCdata(templateWorkingCopy,
                "[INSERT_PBC_CHANNEL_NAME_DK]",
                program.getChannelName());
        templateWorkingCopy = replaceEnclosedInCdata(templateWorkingCopy,
                "[INSERT_PBC_CHANNEL_NAME_DK_ROLE]",
                "kanalnavn");

        templateWorkingCopy = replaceEnclosedInCdata(templateWorkingCopy,
                "[INSERT_PBC_CHANNEL_NAME_ENG]",
                program.getChannelName());
        templateWorkingCopy = replaceEnclosedInCdata(templateWorkingCopy,
                "[INSERT_PBC_CHANNEL_NAME_ENG_ROLE]",
                "channel_name");

        templateWorkingCopy = replaceEnclosedInCdata(templateWorkingCopy,
                "[INSERT_PBC_PROGRAM_LENGTH]",
                program.getScheduledduration());

        // This will be Ritzau data (for web-frontend reasons), not TVMeter.
        templateWorkingCopy = replaceEnclosedInCdata(templateWorkingCopy,
                "[INSERT_PBC_START_TIME]",
                formatDate(program.getScheduledstart()));
        // Rather than tvmeterProgram.getStartDate()
        templateWorkingCopy = replaceEnclosedInCdata(templateWorkingCopy,
                "[INSERT_PBC_END_TIME]",
                formatDate(program.getScheduledend()));
        // Rather than tvmeterProgram.getEndDate()

        return templateWorkingCopy;
    }

    /**
     * Encloses the given replacement in CDATA "tags", and replaces every
     * placeholder in template by it. Also makes sure that if the replacement is
     * "null", then the empty string is inserted instead.
     *
     * @param template The string in which to do replacements.
     * @param placeholder The string of which every occurrence should be
     * replaced.
     * @param replacement The string which should be enclosed in CDATA "tags"
     * and inserted at every occurrence of the placeholder.
     * @return The template with every occurrence of the placeholder replaced by
     * the replacement enclosed in CDATA "tags".
     */
    private String replaceEnclosedInCdata(String template,
                                          String placeholder,
                                          String replacement) {
        log.debug("Entered method replaceEnclosedInCdata('"
                + abbreviatedLongText(template)
                + "', '" + placeholder + "', '" + replacement + "')");

        if (replacement == null || replacement.trim().equalsIgnoreCase("null")
                || replacement.trim().equalsIgnoreCase("null.")) {
            replacement = "";
        }

        return replacePlaceholder(template, placeholder,
                encloseStringInCdataForXml(replacement));
    }

    /**
     * Replaces every occurrence of the given placeholder in the given template
     * with the given replacement.
     *
     * @param template The template in which replacements should be made.
     * @param placeholder The placeholder that should be replaced.
     * @param replacement The replacement for occurrences of the placeholder.
     * @return The input template, with every occurrence of the placeholder
     * replaced by the given replacement.
     */
    private String replacePlaceholder(String template,
                                      String placeholder,
                                      String replacement) {
        log.debug("Entered method replacePlaceholder('"
                + abbreviatedLongText(template)
                + "', '" + placeholder + "', '" + replacement + "')");
        String result;
        CharSequence placeholderNonRegex = placeholder;
        CharSequence replacementAsCharSeq;

        if (template == null) {
            result = "";
        } else if (placeholder == null || placeholder.equals("")) {
            result = template;
        } else if (replacement == null) {
            replacementAsCharSeq = "";
            result = template.replace(placeholderNonRegex,
                    replacementAsCharSeq);
        } else {
            replacementAsCharSeq = replacement;
            result = template.replace(placeholderNonRegex,
                    replacementAsCharSeq);
        }
        log.debug("Returning '" + abbreviatedLongText(result) + "'");
        return result;
    }

    /**
     * Enclose the given text in CDATA "tags", after converting any occurrence
     * of "]]>" in the text to "] ] >", and fixing chars that are illegal in
     * xml.
     *
     * @param text The text to be enclosed in CDATA "tags"
     * @return The input text enclosed in CDATA "tags", and with no illegal
     * chars inside.
     */
    private String encloseStringInCdataForXml(String text) {
        String textWithNoBadStuff = replacePlaceholder(text, "]]>", "] ] >");

        String illegalInXML = "["
                + "\u0001-\u0008" + "\u000B-\u000C" + "\u000E-\u001F"
                + "\u007F-\u0084" + "\u0086-\u009F" //+ "\uFDD0-\uFDDF"
//                + "\u1FFFE-\u1FFFF" + "\u2FFFE-\u2FFFF" + "\u3FFFE-\u3FFFF"
//                + "\u4FFFE-\u4FFFF" + "\u5FFFE-\u5FFFF" + "\u6FFFE-\u6FFFF"
//                + "\u7FFFE-\u7FFFF" + "\u8FFFE-\u8FFFF" + "\u9FFFE-\u9FFFF"
//                + "\uAFFFE-\uAFFFF" + "\uBFFFE-\uBFFFF" + "\uCFFFE-\uCFFFF"
//                + "\uDFFFE-\uDFFFF" + "\uEFFFE-\uEFFFF" + "\uFFFFE-\uFFFFF"
//                + "\u10FFFE-\u10FFFF"
                + "]+";

        // Replace illegal characters with space
        textWithNoBadStuff = textWithNoBadStuff.replaceAll(illegalInXML, " ");

        return "<![CDATA[" + textWithNoBadStuff + "]]>";
        //return textWithNoBadStuff;
    }

    /**
     * Abbreviates the given text for logging purposes.
     *
     * @param text The text to be abbreviated.
     * @return The abbreviated text, with length indicator if truncated.
     */
    private static String abbreviatedLongText(String text) {
        if (text == null) {
            text = "";
        }
        return text.length() > 20
                ? (text.substring(0, 20) + "..." + "(length: " + text.length()
                + ")")
                : text;
    }

    /**
     * TODO javadoc
     *
     * @param program
     * @param templateWorkingCopy
     * @return
     */
    private String insertNormalizedAuthors(
            RitzauProgram program, String templateWorkingCopy) {
        String generatedXmlForAuthors = "";
        List<String> normalizedAuthorNames;

        // Gather list of normalized author names
        normalizedAuthorNames = extractNormalizedNames(program.getForfatter());

        if (normalizedAuthorNames.size() == 0) {
            normalizedAuthorNames.add("");
        }

        for (String normalizedAuthorName : normalizedAuthorNames) {
            String authorTemplate = "\n            <pbcoreCreator>\n"
                    + "                <creator>[INSERT_PBC_AUTHOR]</creator>\n"
                    + "                <creatorRole>[INSERT_PBC_AUTHOR_ROLE]"
                    + "</creatorRole>\n"
                    + "            </pbcoreCreator>\n";

            authorTemplate = replaceEnclosedInCdata(authorTemplate,
                    "[INSERT_PBC_AUTHOR]",
                    normalizedAuthorName);
            authorTemplate = replaceEnclosedInCdata(authorTemplate,
                    "[INSERT_PBC_AUTHOR_ROLE]",
                    "forfatter");

            generatedXmlForAuthors += authorTemplate;
        }

        return replacePlaceholder(templateWorkingCopy,
                "[INSERT_PBC_AUTHORS]",
                generatedXmlForAuthors);
    }

    /**
     * TODO javadoc
     *
     * @param program
     * @param templateWorkingCopy
     * @return
     */
    private String insertNormalizedInstruction(
            RitzauProgram program, String templateWorkingCopy) {
        String generatedXmlForInstructors = "";
        List <String> normalizedInstructorNames = new ArrayList<String>();

        // Gather list of normalized instructor names
        normalizedInstructorNames = extractNormalizedNames(
                program.getInstruktion());

        if (normalizedInstructorNames.size() == 0) {
            normalizedInstructorNames.add("");
        }

        for (String normalizedInstructorName : normalizedInstructorNames) {
            String instructorTemplate = "\n            <pbcoreContributor>\n"
                    + "                <contributor>[INSERT_PBC_INSTRUCTION]"
                    + "</contributor>\n"
                    + "                <contributorRole>"
                    + "[INSERT_PBC_INSTRUCTION_ROLE]</contributorRole>\n"
                    + "            </pbcoreContributor>\n";

            instructorTemplate = replaceEnclosedInCdata(instructorTemplate,
                    "[INSERT_PBC_INSTRUCTION]",
                    normalizedInstructorName);
            instructorTemplate = replaceEnclosedInCdata(instructorTemplate,
                    "[INSERT_PBC_INSTRUCTION_ROLE]",
                    "instruktion");

            generatedXmlForInstructors += instructorTemplate;
        }

        return replacePlaceholder(templateWorkingCopy,
                "[INSERT_PBC_INSTRUCTORS]",
                generatedXmlForInstructors);
    }

    /**
     * TODO javadoc
     *
     * @param program
     * @param templateWorkingCopy
     * @return
     */
    private String insertNormalizedContributors(
            RitzauProgram program, String templateWorkingCopy) {
        String generatedXmlForContributors = "";
        List <String> normalizedContributors = new ArrayList<String>();

        // Gather list of normalized contributors
        normalizedContributors = extractNormalizedNames(
                program.getMedvirkende());

        if (normalizedContributors.size() == 0) {
            normalizedContributors.add("");
        }

        for (String normalizedContributor : normalizedContributors) {
            String contributorTemplate = "\n            <pbcoreContributor>\n"
                    + "                <contributor>[INSERT_PBC_CONTRIBUTOR]"
                    + "</contributor>\n"
                    + "                <contributorRole>"
                    + "[INSERT_PBC_CONTRIBUTOR_ROLE]</contributorRole>\n"
                    + "            </pbcoreContributor>\n";

            contributorTemplate = replaceEnclosedInCdata(contributorTemplate,
                    "[INSERT_PBC_CONTRIBUTOR]",
                    normalizedContributor);
            contributorTemplate = replaceEnclosedInCdata(contributorTemplate,
                    "[INSERT_PBC_CONTRIBUTOR_ROLE]",
                    "medvirkende");

            generatedXmlForContributors += contributorTemplate;
        }

        return replacePlaceholder(templateWorkingCopy,
                "[INSERT_PBC_CONTRIBUTORS]",
                generatedXmlForContributors);
    }

    /**
     * Via heuristics, this method splits a String with a list of names from
     * Ritzau. The names are usually separated by commas, except at the end,
     * where "og" is used. Also the ending "." is removed.
     *
     * @param ritzauNames The list of names as a String, in Ritzau's own format
     * @return The list of separated names
     * @throws java.util.regex.PatternSyntaxException In case there is a bug in our pattern for
     * describing the Ritzau format.
     */
    private static List <String> extractNormalizedNames(
            String ritzauNames) throws PatternSyntaxException {
        List <String> names = new ArrayList<String>();
        String toBeSplit = ritzauNames;
        StringListStringPair stringListStringPair;
        String patternDefinition;
        Pattern pattern;
        Matcher matcher;
        boolean matchFound;

        if (ritzauNames == null || ritzauNames.equals("")) {
            return names;
        }

        if (ritzauNames.contains(" efter ")) {
            // For example:
            // forfatter='Abraham Polonsky og Henri Simoun efter roman af Richard Dougherty.'
            // We can't handle everything
            names.add(ritzauNames);
            return names;
        }

        // First, split on "og"
        patternDefinition = "(.*)(\\sog\\s)(.*)[.]";
        pattern = Pattern.compile(patternDefinition);
        matcher = pattern.matcher(toBeSplit);
        matchFound = matcher.find();

        if (matchFound) {
            // "og" was found, split out the last contributor
            names.add(matcher.group(3));

            toBeSplit = matcher.group(1);
            // and continue with the first part
        } else {
            // "og" was not found, probably there is only one name,
            // so toBeSplit is still the same (but we remove a final ".")
            if (toBeSplit.endsWith(".")) {
                toBeSplit = toBeSplit.substring(0, toBeSplit.length() - 1);
            }
        }

        // Perform the rest of the splitting
        stringListStringPair = eatNamesFromList(new StringListStringPair(
                names, toBeSplit));

        names = stringListStringPair.getStrList();
        return names;
    }

    /**
     * TODO javadoc
     *
     * @param stringListStringPair
     * @return
     */
    private static StringListStringPair eatNamesFromList(
            StringListStringPair stringListStringPair) {
        List <String> names = stringListStringPair.getStrList();
        String toBeSplit = stringListStringPair.getStr();

        String patternDefinition;
        Pattern pattern;
        Matcher matcher;
        boolean matchFound;

        if (toBeSplit == "") {
            // We are done, finished recursing
            return stringListStringPair;
        }

        // Split on ", " and call recursively
        patternDefinition = "(.*)(,[\\s]*)([^,]*$)";
        pattern = Pattern.compile(patternDefinition);
        matcher = pattern.matcher(toBeSplit);
        matchFound = matcher.find();

        if (matchFound) {
            names.add(matcher.group(3));
            toBeSplit = matcher.group(1);

            return eatNamesFromList(new StringListStringPair(
                    names, toBeSplit));
        } else {
            // ", " was not found, probably there is only one name left,
            // so we are finished recursing
            names.add(toBeSplit);
            return new StringListStringPair(names, "");
        }
    }

    /**
     * Formats the date to the ISO8601 format.
     *
     * @param date A date
     * @return The date in a string formatted in ISO 8601 format
     */
    private static String formatDate(Date date) {
        SimpleDateFormat ISO8601FORMAT
                = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

        return ISO8601FORMAT.format(date);
    }
}
