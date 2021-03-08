package com.meowlomo.vmc.task;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;


public class ContextConstant {
	public static final String ALL = "ALL";

    public static final int TAB_NAME_ROW_ID = 0;

    //for element option
    public static final String ELEMENT_REPLACE_LOCATOR_VALUE			= "ELE_REPLACE_LOCATOR_VALUE";
//    public static final String ELEMENT_REPLACE_DATA						= "ELE_REPLACE_DATA";

    //new format with name, type, id or css
    public static final int OBJECT_TYPE_ROW_ID 							= 1;
    public static final int OBJECT_LOCATOR_TYPE_ROW_ID					= 2;
    public static final int OBJECT_LOCATOR_STRING_ROW_ID				= 3;
    //instruction options
    public static final String RESULT_IGNORE 							= "RES_IGNORE";//"RESULT_IGNORE";
    public static final String INSTRUCTION_IGNORE 						= "INS_IGNORE";//"IGNORE";
    public static final String INSTRUCTION_STOP 						= "SYS_STOP";//"STOP";
    public static final String INSTRUCTION_INGORE_DISABLED_BUTTON 		= "BTN_IGNORE_DISABLED";//"IGNORE_DISABLED_BUTTON";
    public static final String INSTRUCTION_BUTTON_UNTIL_DISABLE 		= "BTN_CLICK_UNTIL_DISABLE";//BUTT_UNTIL_DISABLE
    public static final String INSTRUCTION_BUTTON_UNTIL_DISAPPEARS 		= "BTN_CLICK_UNTIL_DISAPPEARS";//"BUTT_UNTIL_DISAPPEARS";
    public static final String INSTRUCTION_BUTTON_UNTIL_POPUP 			= "BTN_CLICK_UNTIL_POPUP";//"BUTT_UNTIL_POPUP";
    public static final String INSTRUCTION_BUTTON_RANDOM_CLICK_ONE 		= "BTN_RANDOM_CLICK_ONE";//"BUTT_RANDOM_CLICK_ONE";
//    public static final String INSTRUCTION_BUTTON_RANDOM_CLICK 			= "BTN_RANDOM_CLICK";//"BUTT_RANDOM_CLICK";
//    public static final String INSTRUCTION_VERIFY_FAILED_EXIT 			= "VERIFY_FAILED_EXIT";


    public static final String INSTRUCTION_BUTTON_AlERT_OK				= "BTN_AlERT_CLICK_OK";//"BUTTON_AlERT_OK";
    public static final String BTN_CLICK_UNTIL_VERIFY_ALERT_TEXT		= "BTN_CLICK_UNTIL_VERIFY_ALERT_TEXT";
    public static final String LNK_CLICK_UNTIL_VERIFY_ALERT_TEXT		= "LNK_CLICK_UNTIL_VERIFY_ALERT_TEXT";
    public static final String INSTRUCTION_BUTTON_AlERT_CANCEL			= "BUTTON_AlERT_NO";
    public static final String INSTRUCTION_BUTTON_AlERT_INFO			= "BUTTON_AlERT_INFO";
    public static final String INSTRUCTION_BUTTON_FIND_TEXT				= "BUTTON_FIND_TEXT";
    public static final String INSTRUCTION_LINK_FIND_TEXT				= "LINK_FIND_TEXT";
    public static final String INSTRUCTION_LINK_AlERT_INFO				= "LINK_AlERT_INFO";

    //compare returnValue with optionValue
    //compare like DTA_COMPARE_RETURN_VALUE:expectValue
    public static final String OPTION_COMPARE_RETURN_VALUE				= "DTA_COMPARE_RETURN_VALUE";//"COMPARE_RETURN_VALUE";

    //save it like
    //SAVE_INPUT:KEY1
    //and use it like $(KEY1)
    public static final String OPTION_SAVE_INPUT						= "DTA_SAVE_INPUT";//"SAVE_INPUT";
    public static final String OPTION_SAVE_TEXT							= "DTA_SAVE_TEXT";//"SAVE_TEXT";
    public static final String OPTION_SAVE_JSONPATH						= "DTA_SAVE_JSONPATH";
    public static final String INSTRUCTION_FAIL_IGNORE_START 			= "RES_IGNORE_FAIL_START";//"FAIL_IGNORE_START";
    public static final String INSTRUCTION_FAIL_IGNORE_END 				= "RES_IGNORE_FAIL_END";//"FAIL_IGNORE_END";
    public static final String INSTRUCTION_NO_RADIO_VERIFY 				= "OPT_NO_RADIO_VERIFY";//"NO_RADIO_VERIFY";
    public static final String INSTRUCTION_NO_VERIFY 					= "NO_VERIFY";//"NO_VERIFY";
    //special key works
    
    //@see INS_IGNORE RES_IGNORE
    //public static final String IGNORE 									= "IGNORE";//"!IGNORE!";
    
    public static final int POPUP_WAIT_MAX 								= 10;
    public static final int WEBELEMENT_WAIT_MAX 						= 20;
    public static final int WEBELEMENT_RETRY_MAX 						= 5;
    public static final String TIME_FORMAT 								= "yyyy-MM-dd";
	//Globe unique string
	private static final String UUID_TIME_FORMAT 						= "MMddHHmmssSSS";
	public static String UUID 											= "";

	//keywords for data input
	public static final String DATA_STAMP 								= "!DateStamp!";

	//info constants
	public static final String SKIP_BY_NOT_FOUND 						= "SkipByNotFound";

    // table selection options
	public static final String TABLE_MODIFY 							= "Modify";
	public static final String TABLE_ADD 								= "Add";
    public static final String TABLE_FIRST_ITEM_ALL_FIELDS_NON_EMPTY 	= "FirstItemAllFieldsNonEmpty";
	public static final String TABLE_FIRST_ITEM 						= "FirstItem";
	public static final String TABLE_BRANCH_LOCATION 					= "Branch Location";
	public static final String TABLE_MAILING_ADDRESS 					= "Mailing Address";
	public static final HashSet<String> LOCATION_LIST = new HashSet<String>(Arrays.asList(new String[]{
			TABLE_BRANCH_LOCATION,
			TABLE_MAILING_ADDRESS
			}));
	public static final Hashtable<String, String> LOCATION_MAPPING = new Hashtable<String, String>() {/**
         * 
         */
        private static final long serialVersionUID = 1L;

    {
					put(TABLE_BRANCH_LOCATION, "(Branch Location)");
					put(TABLE_MAILING_ADDRESS, "(Account General Mailing)");
			}};


	//directory valuables
	public static String SYSTEM_ROOT_DIRECTORY 				= "";

    //runtime valuables for test
	public static String TEST_CASE_RESULT_FOLDER 			= "";
	public static String REMOTE_TEST_CASE_RESULT_FOLDER		= "";
	public static String REMOTE_INSTRUCTION_RESULT_FOLDER	= "";
    public static String LOG_FOLDER 						= "";
    public static String CURRENT_FOLDER 					= "";

    //TestCase valuables
	public static int TEST_CASE_ID 							= 0;
	public static int TEST_CASE_STEP_ID 					= 0;
	public static String TEST_CASE_NAME 					= "";
    public static int EXCEL_ROW_NUMBER 						= 0;
    public static String EXCEL_ROW_NUMBER_STRING 			= "";
    public static String INSTRUCTION 						= "";
    public static String INPUT 								= "";
    
    public static void updateInstructionIndexString(String excelRowId) {
    	excelRowId = excelRowId == null ? "" : excelRowId;
		EXCEL_ROW_NUMBER = Integer.parseInt(excelRowId);
		// format digit
		String format = "%1$04d";
		excelRowId = String.format(format, ContextConstant.EXCEL_ROW_NUMBER);
		ContextConstant.EXCEL_ROW_NUMBER_STRING = excelRowId;
    }
    
    public static String normalIndexName() {
    	return String.format("[%1$s][%2$s]", EXCEL_ROW_NUMBER_STRING, INSTRUCTION);
    }
    
    public static String tailIndexName(String tail) {
    	return String.format("[%1$s][%2$s]%3$s", EXCEL_ROW_NUMBER_STRING, INSTRUCTION, tail);
    }

    public static final String INPUT_CONVERTER_PATTERN_PREFIX	= "${";
    public static final String INPUT_CONVERTER_PATTERN_INT		= "int.random()";
    public static final String INPUT_CONVERTER_PATTERN_DOUBLE	= "double.random()";
    public static final String INPUT_CONVERTER_PATTERN_CHINESE	= "chinese.random()";
    public static final String INPUT_CONVERTER_PATTERN_LETTER	= "lettermix.random()";
    public static final String INPUT_CONVERTER_PATTERN_LETTER1	= "letterlowercase.random()";
    public static final String INPUT_CONVERTER_PATTERN_LETTER2	= "lettercapital.random()";
    public static final String INPUT_CONVERTER_PATTERN_LETTERINTMIX	= "letterintmix.random()";
    public static final String INPUT_CONVERTER_PATTERN_SPECIALSYMBOL= "specialsymbol.random()";
    public static final String INPUT_CONVERTER_PATTERN_SUFFIX	= "}";
    public static final String INPUT_CONVERTER_TIME_CURRENT		= "timeCurrent.random()";
    
}
