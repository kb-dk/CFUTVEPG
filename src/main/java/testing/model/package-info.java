@XmlJavaTypeAdapters({
        @XmlJavaTypeAdapter(value=TimestampAdapter.class,type=Timestamp.class)
})

package testing.model;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import java.sql.Timestamp;