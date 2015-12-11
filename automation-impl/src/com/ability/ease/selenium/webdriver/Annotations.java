package com.ability.ease.selenium.webdriver;

import java.lang.reflect.Field;

import org.openqa.selenium.By;

public class Annotations extends org.openqa.selenium.support.pagefactory.Annotations {

	private final Field field;
	public Annotations(Field field) {
		super(field);
		this.field = field;
	}

	@Override
	public By buildBy() {
		ElementInFrameList framelist;
		if ((framelist = field.getAnnotation(ElementInFrameList.class)) != null) {
			return new ByFramePath(super.buildBy(), framelist.value());
		}
		else {
			return super.buildBy();
		}
	}

}

