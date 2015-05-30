package com.github.hatimiti.flutist.common.domain.type;

import static com.github.hatimiti.flutist.common.util._Obj.*;

import com.github.hatimiti.flutist.common.domain.supports.Condition;
import com.github.hatimiti.flutist.common.domain.supports.InputAttribute;
import com.github.hatimiti.flutist.common.message.AppMessagesContainer;
import com.github.hatimiti.flutist.common.util._Obj;
import com.github.hatimiti.flutist.common.validation.Vval;
import com.github.hatimiti.flutist.common.validation.validator.RequiredFieldValidator;

public abstract class FormType
		extends Type<String> {

	@Condition
	protected String val;

	protected InputAttribute inputAttribute;
	protected String propertyName;
	protected String label;

	protected boolean isRequiredCheckTarget;

	public FormType(
			final InputAttribute inputAttribute,
			final String propertyName,
			final String label) {

		if (_Obj.isEmpty(propertyName)
				|| _Obj.isEmpty(label)) {
			 throw new IllegalArgumentException("propertyName, label は必須です。");
		}

		this.inputAttribute = inputAttribute;
		this.propertyName = propertyName;
		this.label = label;

		if (eq(InputAttribute.REQUIRED, inputAttribute)) {
			this.isRequiredCheckTarget = true;
		}
	}

	@Override
	public String getVal() {
		return this.val;
	}

	public void setStrictVal(final String val) {
		String tmp = this.val;
		this.val = val;
		if (!isValidVal()) {
			this.val = tmp;
			throw new IllegalStateException("ドメイン型に適した値ではありません。");
		}
	}

	public void valid(final AppMessagesContainer container) {
		valid(container, (String) null, (Integer) null);
	}

	public void valid(final AppMessagesContainer container, final String name) {
		valid(container, name, (Integer) null);
	}

	public void valid(final AppMessagesContainer container, final String name, final Integer idx) {
		if (this.isRequiredCheckTarget) {
			new RequiredFieldValidator(container).check(Vval.of(getVal()), getProperty(name, idx), this.label);
		}
		customValid(container, getProperty(name, idx));
	}

	protected String getProperty(final String name, final Integer idx) {

		StringBuilder pn = new StringBuilder();
		if (isNotEmpty(name)) {
			pn.append(name);
		}
		if (isNotEmpty(idx)) {
			pn.append("[" + idx + "]");
		}
		if (isNotEmpty(name) || isNotEmpty(idx)) {
			pn.append(".");
		}

		pn.append(this.propertyName + ".val");
		return pn.toString();
	}

	protected void checkValidVal() {
		if (!isValidVal()) {
			throw new IllegalStateException("ドメイン型に適した値ではありません。");
		}
	}

	protected boolean isValidVal() {
		AppMessagesContainer container = new AppMessagesContainer();
		customValid(container, "");
		return container.isEmpty();
	}

	public FormType inCompleteRequiredCondition() {

		if (ne(InputAttribute.CONDITION, this.inputAttribute)) {
			throw new IllegalStateException("InputAttribute.CONDITIONの場合のみ呼び出しが可能です。");
		}

		this.isRequiredCheckTarget = true;
		return this;
	}

	public FormType notInCompleteRequiredCondition() {

		if (ne(InputAttribute.CONDITION, this.inputAttribute)) {
			throw new IllegalStateException("InputAttribute.CONDITIONの場合のみ呼び出しが可能です。");
		}

		this.isRequiredCheckTarget = false;
		return this;
	}

	public abstract int getLength();
	protected abstract void customValid(AppMessagesContainer container, String propertyName);

	@Override
	public String toString() {
		return super.toString() + " : val = " + getVal();
	}

}
