package kr.co.hs.app;

/**
 * 생성된 시간 2017-01-18, Bae 에 의해 생성됨
 * 프로젝트 이름 : HsCommon
 * 패키지명 : kr.co.hs.app
 */

public interface OnActivityLifeCycleListener {
    void onActivityCreateStatus();
    void onActivityStartStatus();
    void onActivityResumeStatus();

    void onActivityPauseStatus();
    void onActivityStopStatus();
    void onActivityDestryStatus();
}
