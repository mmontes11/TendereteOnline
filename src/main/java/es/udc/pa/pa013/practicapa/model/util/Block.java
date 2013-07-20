package es.udc.pa.pa013.practicapa.model.util;

import java.util.List;

public class Block<T> {

	private List<T> list;
	private boolean existMore;

	public Block(List<T> list, boolean existMore) {
		this.list = list;
		this.existMore = existMore;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public boolean getExistMore() {
		return existMore;
	}

	public void setExistMore(boolean existMore) {
		this.existMore = existMore;
	}
}
