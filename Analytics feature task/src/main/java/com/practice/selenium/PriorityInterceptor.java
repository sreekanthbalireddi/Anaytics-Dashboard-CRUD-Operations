package com.practice.selenium;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.testng.IMethodInstance;
import org.testng.IMethodInterceptor;
import org.testng.ITestContext;

/**
 * Orders methods by TestNG {@code @Test(priority=…)} when set; otherwise by class-level {@link Priority}.
 */
public class PriorityInterceptor implements IMethodInterceptor {

	@Override
	public List<IMethodInstance> intercept(List<IMethodInstance> methods, ITestContext context) {
		Comparator<IMethodInstance> comparator = Comparator.comparingInt(this::resolvePriority);
		IMethodInstance[] array = methods.toArray(new IMethodInstance[0]);
		Arrays.sort(array, comparator);
		return Arrays.asList(array);
	}

	private int resolvePriority(IMethodInstance mi) {
		int fromTestNg = mi.getMethod().getPriority();
		if (fromTestNg != 0) {
			return fromTestNg;
		}
		Class<?> realClass = mi.getMethod().getRealClass();
		Priority ann = realClass.getAnnotation(Priority.class);
		return ann != null ? ann.value() : 0;
	}
}
