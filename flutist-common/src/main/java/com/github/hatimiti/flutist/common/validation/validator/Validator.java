package com.github.hatimiti.flutist.common.validation.validator;

import com.github.hatimiti.flutist.common.message.Owner;
import com.github.hatimiti.flutist.common.validation.Vval;



public interface Validator {

	boolean check(
			final Vval value,
			final Owner owner,
			final Object... params);

}
