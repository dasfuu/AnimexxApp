package de.meisterfuu.animexx.api;

import java.util.List;

import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.objects.UserSearchResultObject;
import de.meisterfuu.animexx.objects.ens.ENSFolderObject;
import de.meisterfuu.animexx.objects.ens.ENSObject;
import de.meisterfuu.animexx.objects.event.EventDescriptionObject;
import de.meisterfuu.animexx.objects.event.EventObject;
import de.meisterfuu.animexx.objects.home.BasicHomeObject;
import de.meisterfuu.animexx.objects.profile.GBEntryObject;
import de.meisterfuu.animexx.objects.profile.GBInfoObject;
import de.meisterfuu.animexx.objects.profile.GBListObject;
import de.meisterfuu.animexx.objects.profile.ProfileObject;
import de.meisterfuu.animexx.objects.rpg.RPGObject;
import de.meisterfuu.animexx.objects.rpg.RPGPostObject;

/**
 * Created by Meisterfuu on 04.10.2014.
 */
public class ApiEvent<T> {

    private T Obj;
    private int callerID;

    public ApiEvent() {

    }

    public ApiEvent(final T pObj, final int pCallerID) {
        Obj = pObj;
        callerID = pCallerID;
    }

    public T getObj() {
        return Obj;
    }

    public ApiEvent<T> setObj(final T pObj) {
        Obj = pObj;
        return this;
    }

    public int getCallerID() {
        return callerID;
    }

    public ApiEvent<T> setCallerID(final int pCallerID) {
        callerID = pCallerID;
        return this;
    }

    public static class ApiProxyEvent {

        private ApiEvent<?> event;

        public ApiEvent<?> getEvent() {
            return event;
        }

        public void setEvent(final ApiEvent<?> pObj) {
            event = pObj;
        }

    }

    public static class ProfileEvent extends ApiEvent<ProfileObject> {
    }

    public static class ENSEvent extends ApiEvent<ENSObject> {
    }

    public static class ENSListEvent extends ApiEvent<List<ENSObject>> {
    }

    public static class ENSFolderEvent extends ApiEvent<ENSFolderObject> {
    }

    public static class RPGEvent extends ApiEvent<RPGObject> {
    }

    public static class RPGListEvent extends ApiEvent<List<RPGObject>> {
    }

    public static class RPGPostEvent extends ApiEvent<RPGPostObject> {
    }

    public static class RPGPostListEvent extends ApiEvent<List<RPGPostObject>> {
    }

    public static class UserEvent extends ApiEvent<UserObject> {
    }

    public static class UserListEvent extends ApiEvent<List<UserObject>> {
    }

    public static class UserSearchEvent extends ApiEvent<UserSearchResultObject> {
    }

    public static class UserSearchListEvent extends ApiEvent<List<UserSearchResultObject>> {
    }

    public static class GBInfoEvent extends ApiEvent<GBInfoObject> {
    }

    public static class GBEntryEvent extends ApiEvent<GBEntryObject> {
    }

    public static class GBEntryListEvent extends ApiEvent<GBListObject> {
    }

    public static class EventEvent extends ApiEvent<EventObject> {
    }

    public static class EventListEvent extends ApiEvent<List<EventObject>> {
    }

    public static class EventDescEvent extends ApiEvent<EventDescriptionObject> {
    }

    public static class EventDescListEvent extends ApiEvent<List<EventDescriptionObject>> {
    }

    public static class HomeEntryEvent extends ApiEvent<BasicHomeObject> {
    }

    public static class HomeEntryListEvent extends ApiEvent<List<BasicHomeObject>> {
    }
}