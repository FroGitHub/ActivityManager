package back.activitymanager.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.Objects;

public class FieldMatchesValidator implements ConstraintValidator<FieldMatches, Object> {
    private String[] fieldNames;

    @Override
    public void initialize(FieldMatches constraintAnnotation) {
        this.fieldNames = constraintAnnotation.fields();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            for (int i = 1; i < fieldNames.length; i++) {
                Field firstField = obj.getClass().getDeclaredField(fieldNames[0]);
                Field currentField = obj.getClass().getDeclaredField(fieldNames[i]);

                firstField.setAccessible(true);
                currentField.setAccessible(true);

                if (!Objects.equals(firstField.get(obj), currentField.get(obj))) {
                    return false;
                }
            }
            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return false;
        }
    }
}
