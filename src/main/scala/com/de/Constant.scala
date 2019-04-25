package com.de

object Constant {

  val predefinedConfiguration = """'additional_col' : [col("post_event_list"), col("post_evar26"),col('post_prop28'),col('post_prop1'),col('post_prop62'),col("post_evar28")],
			'events'	: [F.when(array_contains(split(col('post_event_list'),','),'255'), 'TDS').otherwise(0),
                           F.when(array_contains(split(col('post_event_list'),','),'259'), 'TDC').otherwise(0),
                           F.when(array_contains(split(col('post_event_list'),','),'216'), 'CCS').otherwise(0),
                           F.when(array_contains(split(col('post_event_list'),','),'223'), 'CCE').otherwise(0),
                           F.when(array_contains(split(col('post_event_list'),','),'256'), 'TDSM').otherwise(0),
						   lit(0), #CBS
						   lit(0), #RQS
						   F.when((col('post_evar26') == 'linkedin'),'SOCL').otherwise(0),"""

  val TAB_DELIMITER = "\t"

  val ONE = "1"

  val LEAD_FLAG = "'lead_col' : F.when((array_contains(split(col(\"CONVERSION_EVENTS\"),\",\"),\"TDC\") |"

  val ENGAGED_FLAG = "'engaged_col' : F.when((generic_engaged_criteria | array_contains(split(col(\"CONVERSION_EVENTS\"),\",\"),'TDS')  | array_contains(split(col(\"CONVERSION_EVENTS\"),\",\"),'TDSM') | array_contains(split(col(\"CONVERSION_EVENTS\"),\",\"),'SOCL') |"

  val POST_EVENT_LIST = "post_event_list"

  val POST_EVAR26 = "post_eVar26"

  val POST_PROP1 = "post_prop1"

  val POST_PROP28 = "post_prop28"
  
  val POST_EVAR28 = "post_eVar28"

}