package jp.swell.controller;

import jp.patasys.common.AtareSysException;
import jp.swell.common.ControllerBase;

public class ApiRoomList extends ControllerBase {

    @Override
    public void doInit() {
        setLoginNeeds(true);
        setHttpNeeds(false);
        setHttpsNeeds(false);
        setUsecache(false);
    }

    @Override
    public void doActionProcess() throws AtareSysException {
        forward("ApiRoomList.jsp");
    }
}
