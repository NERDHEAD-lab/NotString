package kr.nerdlab.util.notstring.handler;

import kr.nerdlab.util.notstring.entity.ImNotStringMetaEntity;

public interface NotToStringEntityHandler<VALUE> extends NotStringHandler<ImNotStringMetaEntity<VALUE>> {
	@Override
	String handle(ImNotStringMetaEntity<VALUE> entity);
}
