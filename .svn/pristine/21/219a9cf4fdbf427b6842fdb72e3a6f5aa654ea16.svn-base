package com.eazytec.webapp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.util.ValidatorUtils;
import org.springframework.validation.Errors;
import org.springmodules.validation.commons.FieldChecks;

import com.eazytec.util.PropertyReader;


/**
 * ValidationUtil Helper class for performing custom validations that
 * aren't already included in the core Commons Validator.
 *
 *
 * @author madan
 */
public class ValidationUtil {
    //~ Methods ================================================================

    /**
     * Validates that two fields match.
     * @param bean
     * @param va
     * @param field
     * @param errors
     */
    public static boolean validateTwoFields(Object bean, ValidatorAction va,
                                            Field field, Errors errors) {
        String value =
            ValidatorUtils.getValueAsString(bean, field.getProperty());
        String sProperty2 = field.getVarValue("secondProperty");
        String value2 = ValidatorUtils.getValueAsString(bean, sProperty2);

        if (!GenericValidator.isBlankOrNull(value)) {
            try {
                if (!value.equals(value2)) {
                    FieldChecks.rejectValue(errors, field, va);
                    return false;
                }
            } catch (Exception e) {
                FieldChecks.rejectValue(errors, field, va);
                return false;
            }
        }

        return true;
    }
    
    
    /**
     * Checks if the field matches the regular expression in the field's mask
     * attribute.
     *
     * @param bean The bean validation is being performed on.
     * @param va The <code>ValidatorAction</code> that is currently being
     * performed.
     * @param field The <code>Field</code> object associated with the current
     * field being validated.
     * @param errors The <code>Errors</code> object to add errors to if any
     * validation errors occur.
     * -param request
     * Current request object.
     * @return true if field matches mask, false otherwise.
     */
    public static boolean validateMask(Object bean, ValidatorAction va,
                                       Field field, Errors errors) {
        String mask = field.getVarValue("fieldMask");
        String value =  ValidatorUtils.getValueAsString(bean, field.getProperty());
        try {
            if (!GenericValidator.isBlankOrNull(value)
                && !match(value, mask)) {
                FieldChecks.rejectValue(errors, field, va);
                return false;
            } else {
                return true;
            }
        }
        catch (Exception e) {
            FieldChecks.rejectValue(errors, field, va);
           // return false;
        }
        return true;
    }
    
    public static boolean validateImage(String imageName){
    	String mask="([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
    	return  match(imageName, mask);   		
    
    }
    
    public static boolean validateFileFormats(String fileName){
    	String fileFormat = (String) PropertyReader.getInstance()
		.getPropertyFromFile("String", "system.file.format");
    	String mask="([^\\s]+(\\.(?i)("+fileFormat+"))$)";
    	return  match(fileName, mask);   		
    
    }
    
    public static boolean match(String value,String mask){
    	Pattern p = Pattern.compile(mask);
    	//Pattern p = Pattern.compile("^[\\w\u4e00-\u9eff\\.]{1,20}$");
    	Matcher m = p.matcher(value);
		return m.matches();
    }
    
	public static void main(String[] args) {
		String value="sl果果_";
		// String mask="^[\\w\u4e00-\u9eff\\.]{1,20}$"; name
		String mask="^[\\-a-zA-Z\\_\u4e00-\u9eff\\s]{1,100}$"; 
	}
}
