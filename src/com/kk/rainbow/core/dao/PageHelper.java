package com.kk.rainbow.core.dao;

public final class PageHelper {
	static ThreadLocal<Page> local = new ThreadLocal();

	public static Page getPage() {
		return (Page) local.get();
	}

	protected static void setPage(Page paramPage) {
		local.set(paramPage);
	}

	public static void remove() {
		local.remove();
	}

	public static void main(String[] paramArrayOfString) {
		for (int i = 0; i < 10; i++) {
			Thread local1 = new Thread() {
				public void run() {
					PageHelper.setPage(new Page((int) getId(), 10));
					System.out.println(getId() + "=="
							+ PageHelper.getPage().getCurrPage() + ":"
							+ PageHelper.getPage().getLimit());
					PageHelper.remove();
					if (PageHelper.getPage() == null)
						System.out.println(getId() + "=page is null");
					else
						System.out.println(getId() + "=page is "
								+ PageHelper.getPage().toString());
				}
			};
			local1.start();
		}
	}
}