package cn.sinobest.framework.web;

public abstract interface IBaseAction {
	public static final String RTN_URL = "_rtnURL";
	public static final String TO_SUCCESS = "success";
	public static final String TO_NONE = "none";
	public static final String TO_ERROR = "error";
	public static final String TO_INPUT = "i";
	public static final String TO_QUERY = "q";
	public static final String TO_UPDATE = "u";
	public static final String TO_MULTI = "m";
	public static final String TO_LOGIN = "login";
	public static final String PARAMS = "_params";
	public static final String VALS = "${dto.getStrParams4Data()}";
}
