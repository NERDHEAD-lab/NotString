package kr.nerdlab.util.notstring.entity;

import kr.nerdlab.util.notstring.env.NotStringType;

public interface NotStringMetaEntity<VALUE> {
	NotStringType type();

	String key();

	String defaultValue();

	VALUE value();
}
