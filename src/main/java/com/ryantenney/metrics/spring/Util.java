/**
 * Copyright (C) 2012 Ryan W Tenney (ryan@10e.us)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ryantenney.metrics.spring;

import static com.codahale.metrics.MetricRegistry.name;

import java.lang.reflect.Member;

import com.codahale.metrics.annotation.CachedGauge;
import com.codahale.metrics.annotation.Counted;
import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Gauge;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Metric;
import com.codahale.metrics.annotation.Timed;

class Util {

	private Util() {}

	static String forTimedMethod(Class<?> klass, Member member, Timed annotation) {
		return chooseName(null, annotation.name(), annotation.absolute(), klass, member);
	}

	static String forMeteredMethod(Class<?> klass, Member member, Metered annotation) {
		return chooseName(null, annotation.name(), annotation.absolute(), klass, member);
	}

	static String forGauge(Object bean, Class<?> klass, Member member, Gauge annotation) {
		return chooseName(bean, annotation.name(), annotation.absolute(), klass, member);
	}

	static String forCachedGauge(Object bean, Class<?> klass, Member member, CachedGauge annotation) {
		return chooseName(bean, annotation.name(), annotation.absolute(), klass, member);
	}

	static String forExceptionMeteredMethod(Class<?> klass, Member member, ExceptionMetered annotation) {
		return chooseName(null, annotation.name(), annotation.absolute(), klass, member, ExceptionMetered.DEFAULT_NAME_SUFFIX);
	}

	static String forCountedMethod(Class<?> klass, Member member, Counted annotation) {
		return chooseName(null, annotation.name(), annotation.absolute(), klass, member);
	}

	static String forMetricField(Class<?> klass, Member member, Metric annotation) {
		return chooseName(null, annotation.name(), annotation.absolute(), klass, member);
	}

	@Deprecated
	static String forCachedGauge(Class<?> klass, Member member, com.ryantenney.metrics.annotation.CachedGauge annotation) {
		return chooseName(null, annotation.name(), annotation.absolute(), klass, member);
	}

	@Deprecated
	static String forCountedMethod(Class<?> klass, Member member, com.ryantenney.metrics.annotation.Counted annotation) {
		return chooseName(null, annotation.name(), annotation.absolute(), klass, member);
	}

	@Deprecated
	static String forMetricField(Class<?> klass, Member member, com.ryantenney.metrics.annotation.Metric annotation) {
		return chooseName(null, annotation.name(), annotation.absolute(), klass, member);
	}

	static String chooseName(Object bean, String explicitName, boolean absolute, Class<?> klass, Member member, String... suffixes) {
		String prefix = "";
		if (bean != null && bean instanceof CustomPrefixConfigurer) {
			CustomPrefixConfigurer customPrefixConfigurer = (CustomPrefixConfigurer) bean;
			prefix = customPrefixConfigurer.getCustomPrefix();
		}

		if (explicitName != null && !explicitName.isEmpty()) {
			if (absolute) {
				return name(prefix, explicitName);
			}
			return name(prefix, klass.getCanonicalName(), explicitName);
		}
		return name(name(prefix, klass.getCanonicalName(), member.getName()), suffixes);
	}

}
