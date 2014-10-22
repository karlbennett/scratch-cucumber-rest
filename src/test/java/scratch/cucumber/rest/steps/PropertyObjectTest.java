package scratch.cucumber.rest.steps;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static java.util.Collections.emptyMap;
import static java.util.Collections.unmodifiableMap;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PropertyObjectTest {

    private static final String KEY_ONE = "one";
    private static final String KEY_TWO = "two";
    private static final String KEY_THREE = "three";
    private static final String KEY_FOUR = "four";
    private static final String KEY_FIVE = "five";
    private static final String KEY_SIX = "six";
    private static final String KEY_SEVEN = "seven";
    private static final String KEY_EIGHT = "eight";
    private static final String KEY_NINE = "nine";

    private static final int VALUE_ONE = 1;
    private static final String VALUE_TWO = "2";
    private static final float VALUE_FOUR = 4.0F;
    private static final char VALUE_FIVE = 'V';
    private static final long VALUE_SEVEN = 7;
    private static final double VALUE_EIGHT = 8.0;
    private static final boolean VALUE_NINE = true;

    private static final Map<String, Object> MAP_SIX = unmodifiableMap(new HashMap<String, Object>() {{
        put(KEY_SEVEN, VALUE_SEVEN);
        put(KEY_EIGHT, VALUE_EIGHT);
        put(KEY_NINE, VALUE_NINE);
    }});

    private static final Map<String, Object> MAP_THREE = unmodifiableMap(new HashMap<String, Object>() {{
        put(KEY_FOUR, VALUE_FOUR);
        put(KEY_FIVE, VALUE_FIVE);
        put(KEY_SIX, MAP_SIX);
    }});

    private static final Map<String, Object> MAP = unmodifiableMap(new HashMap<String, Object>() {{
        put(KEY_ONE, VALUE_ONE);
        put(KEY_TWO, VALUE_TWO);
        put(KEY_THREE, MAP_THREE);
    }});

    private static final String PROPERTY_FOUR = format("%s.%s", KEY_THREE, KEY_FOUR);
    private static final String PROPERTY_FIVE = format("%s.%s", KEY_THREE, KEY_FIVE);
    private static final String PROPERTY_SIX = format("%s.%s", KEY_THREE, KEY_SIX);
    private static final String PROPERTY_SEVEN = format("%s.%s.%s", KEY_THREE, KEY_SIX, KEY_SEVEN);
    private static final String PROPERTY_EIGHT = format("%s.%s.%s", KEY_THREE, KEY_SIX, KEY_EIGHT);
    private static final String PROPERTY_NINE = format("%s.%s.%s", KEY_THREE, KEY_SIX, KEY_NINE);

    @Test
    public void I_can_create_an_empty_property_object() {

        assertEquals("the property object should be empty.", emptyMap(), new PropertyObject().toMap());
    }

    @Test
    public void I_can_create_a_property_object_with_a_map() {

        assertEquals("the property objects map should be correct.", MAP, new PropertyObject(MAP).toMap());
    }

    @Test
    public void I_can_create_a_property_object_with_another_property_object() {

        final PropertyObject object = new PropertyObject(MAP);

        assertEquals("the property objects should be equal.", object, new PropertyObject(object));
    }

    @Test(expected = NullPointerException.class)
    public void I_cannot_create_a_property_object_from_a_null_map() {

        new PropertyObject((Map<String, Object>) null);
    }

    @Test(expected = NullPointerException.class)
    public void I_cannot_create_a_property_object_from_a_null_property_object() {

        new PropertyObject((PropertyObject) null);
    }

    @Test
    public void I_can_get_a_value_from_a_property_object() {

        final PropertyObject object = new PropertyObject(MAP);

        // Also testing auto casting.
        final int one = object.get(KEY_ONE);
        final char five = object.get(PROPERTY_FIVE);
        final boolean nine = object.get(PROPERTY_NINE);

        assertEquals("value one should be correct.", VALUE_ONE, one);
        assertEquals("value five should be correct.", VALUE_FIVE, five);
        assertEquals("value nine should be correct.", VALUE_NINE, nine);
    }

    @Test
    public void I_can_get_a_property_object_from_a_property_object() {

        final PropertyObject object = new PropertyObject(MAP);

        final PropertyObject three = object.get(KEY_THREE);
        final PropertyObject six = object.get(PROPERTY_SIX);

        assertEquals("property object three should be correct.", new PropertyObject(MAP_THREE), three);
        assertEquals("property object six should be correct.", new PropertyObject(MAP_SIX), six);

        assertEquals("value four should be correct.", VALUE_FOUR, three.get(KEY_FOUR));
        assertEquals("value eight six should be correct.", VALUE_EIGHT, six.get(KEY_EIGHT));
    }

    @Test
    public void I_cannot_get_an_invalid_property() {

        final PropertyObject object = new PropertyObject(MAP);

        assertNull("no value should be retrieved for an invalid property.", object.get("invalid"));
        assertNull("no value should be retrieved for an invalid property.", object.get("still.invalid"));
        assertNull("no value should be retrieved for an invalid property.", object.get("yep.still.invalid"));
    }

    @Test
    public void I_can_set_a_property_value() {

        final PropertyObject object = new PropertyObject(MAP);

        final String propertyTwoDotOne = format("%s.%s", KEY_TWO, "two dot one");

        final String one = "Jan";
        final double twoDotOne = 2.1;
        final int five = 5;
        final boolean nine = false;

        object.set(KEY_ONE, one);
        object.set(propertyTwoDotOne, twoDotOne);
        object.set(PROPERTY_FIVE, five);
        object.set(PROPERTY_NINE, nine);

        assertEquals("value one should be set correct.", one, object.get(KEY_ONE));
        assertEquals("value two dot one should be set correct.", twoDotOne, object.get(propertyTwoDotOne));
        assertEquals("value five should be set correct.", five, object.get(PROPERTY_FIVE));
        assertEquals("value nine should be set correct.", nine, object.get(PROPERTY_NINE));
    }

    @Test
    public void I_can_create_a_property_value() {

        final PropertyObject object = new PropertyObject(MAP);

        final String keyTen = "ten";
        final String keyEleven = "eleven";
        String propertyTwelve = format("%s.%s", keyEleven, "twelve");
        String propertyFourteen = format("%s.%s.%s", keyEleven, "thirteen", "fourteen");

        final char ten = 'X';
        final int twelve = 12;
        final float fourteen = 1.4F;

        object.set(keyTen, ten);
        object.set(propertyTwelve, twelve);
        object.set(propertyFourteen, fourteen);

        assertEquals("value ten should be set correct.", ten, object.get(keyTen));
        assertEquals("value twelve should be set correct.", twelve, object.get(propertyTwelve));
        assertEquals("value fourteen should be set correct.", fourteen, object.get(propertyFourteen));
    }

    @Test
    public void I_can_remove_a_property_value() {

        final PropertyObject object = new PropertyObject(MAP);

        object.remove(KEY_TWO);
        object.remove(PROPERTY_FOUR);
        object.remove(PROPERTY_SEVEN);

        assertNull("value two should be removed.", object.get(KEY_TWO));
        assertNull("value four should be removed.", object.get(PROPERTY_FOUR));
        assertNull("value seven should be removed.", object.get(PROPERTY_SEVEN));
    }

    @Test
    public void I_can_remove_a_property_value_from_one_object_and_it_will_not_be_removed_from_a_related_object() {

        final PropertyObject object = new PropertyObject(MAP);
        final PropertyObject objectCopy = new PropertyObject(object);
        final PropertyObject objectThree = new PropertyObject((PropertyObject) object.get(KEY_THREE));
        final PropertyObject objectSix = new PropertyObject((PropertyObject) object.get(PROPERTY_SIX));

        object.remove(PROPERTY_EIGHT);

        assertNull("value eight should be removed from parent object.", object.get(PROPERTY_EIGHT));

        assertEquals("value eight should still exist in copy.", VALUE_EIGHT, objectCopy.get(PROPERTY_EIGHT));
        assertEquals("value eight should still exist in object three.", VALUE_EIGHT,
                objectThree.get(format("%s.%s", KEY_SIX, KEY_EIGHT)));
        assertEquals("value eight should still exist in object six.", VALUE_EIGHT, objectSix.get(KEY_EIGHT));
    }

    @Test
    public void I_can_clear_a_property_object() {

        final PropertyObject object = new PropertyObject(MAP);
        object.clear();

        assertEquals("the property object should be empty.", new PropertyObject(), object);
        assertEquals("the property object should be empty.", emptyMap(), object.toMap());
    }

    @Test
    public void I_can_check_the_equality_of_a_property_object() {

        I_can_check_the_equality_of_a_property_object(new Equals<PropertyObject>() {
            @Override
            public boolean equal(PropertyObject left, Object right) {
                return left.equals(right);
            }
        });
    }

    @Test
    public void I_can_check_the_hash_code_of_a_property_object() {

        I_can_check_the_equality_of_a_property_object(new Equals<PropertyObject>() {
            @Override
            public boolean equal(PropertyObject left, Object right) {

                if (null == right) {
                    return false;
                }

                return left.hashCode() == right.hashCode();
            }
        });
    }

    @Test
    public void I_can_to_string_a_property_object() {

        assertEquals("the property objects to string should be correct.", MAP.toString(),
                new PropertyObject(MAP).toString());
    }

    private static void I_can_check_the_equality_of_a_property_object(Equals<PropertyObject> eq) {

        final PropertyObject left = new PropertyObject(MAP);
        final PropertyObject right = new PropertyObject(left);

        assertTrue("a property object should be equal to it's self.", eq.equal(left, left));
        assertTrue("a property object should be equal to a copy of it's self.", eq.equal(left, right));

        right.remove(PROPERTY_SEVEN);
        assertFalse("a property object should not be equal to a different property object.", eq.equal(left, right));
        assertFalse("a property object should not be equal an object.", eq.equal(left, new Object()));
        assertFalse("a property object should not be equal null.", eq.equal(left, null));
    }


    private static interface Equals<T> {
        public boolean equal(T left, Object right);
    }
}
