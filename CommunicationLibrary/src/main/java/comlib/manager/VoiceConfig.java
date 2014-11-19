package comlib.manager;

import rescuecore2.config.Config;

public class VoiceConfig {

    private int ttl;

    private String keyword;

    private String messageSeparator;

    private String dataSeparator;

    public VoiceConfig(Config config) {
        this.ttl = config.getIntValue("comlib.message.ttl", 10);
        this.keyword = config.getValue("comlib.message.keyword", "Voice");
        this.messageSeparator = config.getValue("comlib.message.messageSeparator", ">");
        this.dataSeparator = config.getValue("comlib.message.dataSeparator", ":");
    }

    public int getTTL() {
        return this.ttl;
    }

    public void setTTL(int value) {
        if (value > 0) {
            this.ttl = value;
        }
    }

    public String getKeyword() {
        return this.keyword;
    }

    public String getMessageSeparator() {
        return this.messageSeparator;
    }

    public String getDataSeparator() {
        return this.dataSeparator;
    }

    public void appendMessageSeparator(StringBuilder sb)
    {
        sb.append(this.messageSeparator);
    }

    public void appendDataSeparator(StringBuilder sb)
    {
        sb.append(this.dataSeparator);
    }

    public void appendData(StringBuilder sb, String data)
    {
        sb.append(data);
        this.appendDataSeparator(sb);
    }

    public void appendMessageID(StringBuilder sb, int id)
    {
        sb.append(id);
        this.appendMessageSeparator(sb);
    }

    public void appendLimit(StringBuilder sb)
    {
        this.appendData(sb, String.valueOf((this.ttl - 1)));
    }
}
    
    
