package games.distetris.domain;

public class L {

	private static Boolean VERBOSE = true;
	private static String TAG = "DISTETRIS";

	public static void d(String s) {
		if (VERBOSE) {
			StackTraceElement element = Thread.currentThread().getStackTrace()[3];
			android.util.Log.d(TAG, "[" + element.getFileName() + "][" + element.getMethodName() + "] " + s);
		}
	}

	public static void d(String s, Exception e) {
		if (VERBOSE) {
			StackTraceElement element = Thread.currentThread().getStackTrace()[3];
			android.util.Log.d(TAG, "[" + element.getFileName() + "][" + element.getMethodName() + "] " + s, e);
		}
	}

	public static void e(String s) {
		if (VERBOSE) {
			StackTraceElement element = Thread.currentThread().getStackTrace()[3];
			android.util.Log.d(TAG, "[" + element.getFileName() + "][" + element.getMethodName() + "] " + s);
		}
	}

	public static void e(String s, Exception e) {
		if (VERBOSE) {
			StackTraceElement element = Thread.currentThread().getStackTrace()[3];
			android.util.Log.d(TAG, "[" + element.getFileName() + "][" + element.getMethodName() + "] " + s, e);
		}
	}
}
