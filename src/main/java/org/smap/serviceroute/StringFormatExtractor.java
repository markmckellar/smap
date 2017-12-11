package org.smap.serviceroute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.smap.util.Log;


public class StringFormatExtractor
{
	public static final String DEF_START_TAG_NAME = "{:";
	public static final String DEF_END_TAG_NAME = ":}";
	public static final String DEF_SEPERATOR = "/";
	//public static final String DEF_TOKEN_PATERN = ":}";
	//public static final String DEF_SEPERATOR_PATERN = ":}";
	private String startTagName;
	private String endTagName;
	private String defSeperator;
	private String formatString;
	private List<String> availableArgList;
	private Map<String,String> extractedValueMap;
	private String patternString;
	
	public static void main(String args[]) throws Exception
	{
		StringFormatExtractor stringFormatExtractor1 = new StringFormatExtractor("/{:arg1:}/{:arg2:}/{:arg3:}/{:arg4:}");
		stringFormatExtractor1.getTagNameToValueMap("/this/is/a/test");
		Log.info("matches="+stringFormatExtractor1.matches("/this/is/a/test"));
		
		
		StringFormatExtractor stringFormatExtractor2 = new StringFormatExtractor("/this/is/a/{:arg4:}");
		stringFormatExtractor2.getTagNameToValueMap("/this/is/a/test");
		Log.info("matches="+stringFormatExtractor2.matches("/this/is/a/test"));
		Log.info("matches="+stringFormatExtractor2.matches("/hummmm/is/a/test"));
		
		
	}
	
	public StringFormatExtractor(String formatString) throws Exception
	{
		this(formatString,DEF_START_TAG_NAME,DEF_END_TAG_NAME,DEF_SEPERATOR);
	}
	
	public StringFormatExtractor(String formatString,String startTagName,String endTagName,String defSeperator) throws Exception
	{
		this.setStartTagName(startTagName);
		this.setEndTagName(endTagName);
		this.setDefSeperator(defSeperator);
		this.setAvailableArgList(new ArrayList<String>());
		this.setFormatString(formatString);
	}
	
	private String createPatternString(String formatString) throws Exception
	{
		List<String> tagNameList = new ArrayList<String>();
		String workString = formatString+"";
		String patternString = "";
		while(workString.indexOf(startTagName)>=0)
        {
            int start = workString.indexOf(startTagName);
            int end = workString.indexOf(endTagName);
            if(end<0) throw new Exception(startTagName+" was found at "+start+" with no matching "+endTagName+" in "+formatString);
            String tagName = workString.substring(start+new String(startTagName).length(),end);
            //patternString += workString.substring(0,start)+"(?<"+tagName+">.*?)";
            patternString += workString.substring(0,start)+"(?<"+tagName+">[^"+getDefSeperator()+"]*?)";
            tagNameList.add(tagName);
            workString = workString.substring(end+new String(endTagName).length(),workString.length());
        }

		patternString += workString+"$";
		return(patternString);
	}
	
	public boolean matches(String text)
	{
		Pattern pattern = Pattern.compile(this.getPatternString());
		Matcher matcher = pattern.matcher(text);
		return(matcher.matches());
	}
	
	public Map<String,String> getTagNameToValueMap(String text) throws Exception
	{
		Map<String,String> tagNameToValueMap = new HashMap<String,String>();

		//Log.info("StringFormatExtractor:patternString="+getPatternString());
		//Log.info("StringFormatExtractor:text="+text);

		Pattern pattern = Pattern.compile(this.getPatternString());
		Matcher matcher = pattern.matcher(text);
		matcher.find();
		
		Log.info("StringFormatExtractor:matcher.groupCount()="+matcher.groupCount());
		/*************
		for(int i=1;i<=matcher.groupCount();i++)
		{
			System.out.println("     StringFormatExtractor:group#="+i);
			System.out.println("     StringFormatExtractor:name="+matcher.group(i));
		}
		***********/
		
		for(String tagName:this.getAvailableArgList())
		{
			Log.info("StringFormatExtractor:matches:tagName="+tagName+":found='"+matcher.group(tagName)+"'");
			tagNameToValueMap.put(tagName,matcher.group(tagName));
		}
		
		return(tagNameToValueMap);
	}
	
		
	private List<String> getTagNameList(String startTagName,String endTagName,String text) throws Exception
	{
		List<String> tagNameList = new ArrayList<String>();
        String workString = text+"";
		while(workString.indexOf(startTagName)>=0)
        {
            int start = workString.indexOf(startTagName);
            int end = workString.indexOf(endTagName);
            if(end<0) throw new Exception(startTagName+" was found at "+start+" with no matching "+endTagName+" in "+text);
            String tagName = workString.substring(start+new String(startTagName).length(),end);
            tagNameList.add(tagName);
            workString = workString.substring(end+new String(endTagName).length(),workString.length());
        }
		return(tagNameList);
	}

	public String getFormatString() {
		return formatString;
	}

	public void setFormatString(String formatString) throws Exception
	{
		this.setAvailableArgList(getTagNameList(getStartTagName(),getEndTagName(),formatString));
		this.setPatternString(createPatternString(formatString));
		this.formatString = formatString;
	}

	public List<String> getAvailableArgList() {
		return availableArgList;
	}

	private void setAvailableArgList(List<String> availableArgList) {
		this.availableArgList = availableArgList;
	}

	public Map<String,String> getExtractedValueMap() {
		return extractedValueMap;
	}

	public void setExtractedValueMap(Map<String,String> extractedValueMap) {
		this.extractedValueMap = extractedValueMap;
	}

	public String getStartTagName() {
		return startTagName;
	}

	public void setStartTagName(String startTagName) {
		this.startTagName = startTagName;
	}

	public String getEndTagName() {
		return endTagName;
	}

	public void setEndTagName(String endTagName) {
		this.endTagName = endTagName;
	}

	public String getPatternString() {
		return patternString;
	}

	private void setPatternString(String patternString) {
		this.patternString = patternString;
	}

	public String getDefSeperator() {
		return defSeperator;
	}

	public void setDefSeperator(String defSeperator) {
		this.defSeperator = defSeperator;
	}


}
