#!/usr/bin/env python

import urllib
import psycopg2
import ConfigParser
import sys
import logging
import datetime
import time
try:
    import xml.etree.cElementTree as ET
except ImportError:
    import xml.etree.ElementTree as ET

def remove_namespace(doc, namespace):
    ns = u'{%s}' % namespace
    nsl = len(ns)
    for elem in doc.getiterator():
        if elem.tag.startswith(ns):
            elem.tag = elem.tag[nsl:]

def connect_to_db():
    print "[INFO]: Connecting to DB"
    conn_string = "host="+Config.get("Database", "host") + " dbname=" + Config.get("Database", "dbname") + " user=" + Config.get("Database", "user") + " password=" + Config.get("Database", "pass")
    conn = psycopg2.connect(conn_string)
    cursor = conn.cursor()
    print "[INFO]: Connected"
    return conn, cursor

def update_progress(progress):
    barLength = 20 # Modify this to change the length of the progress bar
    status = ""
    if isinstance(progress, int):
        progress = float(progress)
    if progress >= 1:
        progress = 1
        status = "Done...\r\n"
    block = int(round(barLength*progress))
    text = "\r[INFO]: Progress: [{0}] {1}% {2}".format( "="*block + " "*(barLength-block), round(progress*100,3), status)
    sys.stdout.write(text)
    sys.stdout.flush()

Config = ConfigParser.ConfigParser()
Config.read("EPG-Parser.conf")
logging.basicConfig(filename=Config.get("EPGLog", "path")+Config.get("EPGLog","filename"),level=logging.INFO)
logging.info("Start - "+datetime.datetime.fromtimestamp(time.time()).strftime('%Y-%m-%d %H:%M:%S'))

print '[INFO]: Downloading EPG xml'
urllib.urlretrieve("http://" + Config.get("EPGLocationRemote", "user") + ":" + Config.get("EPGLocationRemote", "pass") + "@" + Config.get("EPGLocationRemote", "epgurl") , Config.get("EPGLocationLocal", "location") + Config.get("EPGLocationLocal", "filename"))
conn, cursor = connect_to_db()

tree = ET.ElementTree(file=Config.get("EPGLocationLocal", "location") + Config.get("EPGLocationLocal", "filename"))
root = tree.getroot()

cmtree = ET.ElementTree(file=Config.get("EPGLocationLocal", "channelmappings"))
cmroot = cmtree.getroot()

parent_map = dict((c, p) for p in tree.getiterator() for c in p)

remove_namespace(tree, u'http://tvservices.microsoft.com/epg/glf')

print "[INFO]: Resetting programs checked status for the last 6 days"
sqlquery = "UPDATE programs SET checked='false' WHERE yearanddate >= (now() - '6 day'::INTERVAL);"
cursor.execute(sqlquery)

print "[INFO]: Creating program map"
programdict = {}
for program in root.findall("./listings/programs/p"):
     programdict[program.get('id')] = program

print "[INFO]: Creating names map"
namesdict = {}
for name in root.findall("./listings/programroles/names/n"):
    namesdict[name.get('id')] = name

print "[INFO]: Creating program categories map"
categoriesDict = {}
for child in root.findall("./listings/programcategories/c"):
    categoriesDict[child.get('id')] = child
for child in root.findall("./listings/programcategories/c/c"):
    categoriesDict[child.get('id')] = child

print "[INFO]: Creating channel map"
channelDict = {}
for child in root.findall("./channels/c"):
    channelDict[child.get('id')] = child

print "[INFO]: Creating channel mappings map"
channelMapDict = {}
for channelit in cmroot.findall('./channel'):
    channelMapDict[channelit.get('ys_epg')] = channelit

print "[INFO]: Parsing schedules, program values and matching programs"
progresCount = 0
scheduleCount = len(root.findall("./listings/schedules/s"))
logging.info("Found "+str(scheduleCount)+" schedules")
sqlquery = "INSERT INTO programs (id, title, reducedTitle, description, reducedDescription, episodeTitle, language, forfra, arkiv, DVB, category, actors, extra, scheduledstart, scheduledDuration, scheduledend, channel_name, ys_epg, ys_download, yearAndDate, checked) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)"
for schedule in root.findall("./listings/schedules/s"):
    progresCount += 1
    update_progress(float(progresCount)/float(scheduleCount))
    program = programdict.get(schedule.get('p'))
    try:
        programValue = program.find('k').get('v')
        programValueId = ""
        for pv in root.findall("./listings/programvalues/pv"):
            if program.find('k').get('id') in pv.get('id'):
                programValueId = pv.get('pname')
                break
        if(programValueId != ""):
            extra = str(programValueId)+": "+str(programValue)
        else:
            raise AttributeError
    except AttributeError:
        extra = None
    try:
        names = None
        for id in program.findall('r'):
            if(names == None):
                names = namesdict.get(id.get('n')).get('fname').encode('utf-8', 'xmlcharrefreplace')
            else:
                names = names + ", " + namesdict.get(id.get('n')).get('fname').encode('utf-8', 'xmlcharrefreplace')
    except AttributeError:
        names = None

    category = categoriesDict.get(program.find('c').get('id')).get('mscname')

    ys_epg = channelDict.get(schedule.get('c')).get('c')
    channel=channelMapDict.get(ys_epg).get('channel_name')
    ys_download=channelMapDict.get(ys_epg).get('ys_download')

    scheduledend = datetime.datetime.strptime(schedule.get('s'), "%Y-%m-%dT%H:%M:%S")+datetime.timedelta(0,int(schedule.get('d')))

    data = (program.get('id'), program.get('t', 'None'), program.get('rt', 'None'), program.get('d', 'None'), program.get('rd', 'None'), program.get('et', 'None'), program.get('l', 'None'), program.get('forfra', 'None'), program.get('arkiv', 'None'), program.get('DVB', 'None'), category, names, extra, schedule.get('s', 'None'), schedule.get('d', 'None'), scheduledend.strftime("%Y-%m-%dT%H:%M:%S"), channel, ys_epg, ys_download, schedule.get('y'), True)
    cursor.execute(sqlquery, data)

print "[INFO]: Deleting programs that was removed from EPG"
cursor.execute ("SELECT COUNT(*) FROM programs WHERE checked='false'")
row = cursor.fetchone()
logging.info("Removing "+str(row)+" programs that were deleted from EPG")
sqlquery = "DELETE FROM programs WHERE checked='false'"
cursor.execute(sqlquery)

print "[INFO]: Committing"
conn.commit()

print "[INFO]: Success"
logging.info("Done")