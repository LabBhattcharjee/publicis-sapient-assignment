package com.sapient.publicis.model;

import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanConstructor;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanEquals;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanHashCode;
import static com.google.code.beanmatchers.BeanMatchers.hasValidBeanToString;
import static com.google.code.beanmatchers.BeanMatchers.hasValidGettersAndSetters;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.sapient.publicis.model.out.WeatherResponse;

class ModelTest {

	private final Set<Class<?>> set = new HashSet<>();

	@Test
	public void testBean0() {
		validateRecusrively(WeatherResponse.class);
		set.clear();
	}

	private void validateRecusrively(final Class<?> class1) {
		if (!set.add(class1)) {
			return;
		}
		assertThat(WeatherResponse.class, allOf(hasValidBeanConstructor(), hasValidGettersAndSetters(),
				hasValidBeanHashCode(), hasValidBeanEquals(), hasValidBeanToString()));

		final Field[] declaredFields = class1.getDeclaredFields();
		for (final Field field : declaredFields) {
			final Class<?> type = field.getType();
			if (type.getName().contains("com.sapient")) {
				validateRecusrively(class1);
			} else if (Iterable.class.isAssignableFrom(type)) {
				final Type genericType = field.getGenericType();
				final String typeName = genericType.getTypeName();
				final String trim = typeName.substring(typeName.indexOf('<') + 1, typeName.indexOf('>')).trim();
				try {
					validateRecusrively(Class.forName(trim));
				} catch (final ClassNotFoundException e) {
					e.printStackTrace();
				}
			} else if (type.isArray()) {
				validateRecusrively(type.getComponentType());
			}
		}

	}

}
