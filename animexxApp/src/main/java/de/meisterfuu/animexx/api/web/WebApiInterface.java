package de.meisterfuu.animexx.api.web;

import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;

import de.meisterfuu.animexx.objects.FileUploadReturnObject;
import de.meisterfuu.animexx.objects.KarotalerStatsObject;
import de.meisterfuu.animexx.objects.SingleValueObjects;
import de.meisterfuu.animexx.objects.UploadedFile;
import de.meisterfuu.animexx.objects.UserObject;
import de.meisterfuu.animexx.objects.aidb.MangaObject;
import de.meisterfuu.animexx.objects.contacts.ContactGroupObject;
import de.meisterfuu.animexx.objects.ens.ENSCheckRecipientsObject;
import de.meisterfuu.animexx.objects.ens.ENSFolderObject;
import de.meisterfuu.animexx.objects.ens.ENSObject;
import de.meisterfuu.animexx.objects.event.EventAttender;
import de.meisterfuu.animexx.objects.event.EventDescriptionObject;
import de.meisterfuu.animexx.objects.event.EventObject;
import de.meisterfuu.animexx.objects.event.EventRoomProgramObject;
import de.meisterfuu.animexx.objects.home.ContactHomeObject;
import de.meisterfuu.animexx.objects.home.HomeCommentObject;
import de.meisterfuu.animexx.objects.photos.PhotoSeriesObject;
import de.meisterfuu.animexx.objects.profile.GBInfoObject;
import de.meisterfuu.animexx.objects.profile.GBListObject;
import de.meisterfuu.animexx.objects.profile.ProfileBoxObject;
import de.meisterfuu.animexx.objects.profile.ProfileObject;
import de.meisterfuu.animexx.objects.rpg.RPGObject;
import de.meisterfuu.animexx.objects.rpg.RPGPostObject;
import de.meisterfuu.animexx.objects.weblog.WeblogCommentObject;
import de.meisterfuu.animexx.objects.weblog.WeblogEntryObject;
import de.meisterfuu.animexx.objects.xmpp.XMPPHistoryObject;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

import static de.meisterfuu.animexx.objects.SingleValueObjects.CalListObject;
import static de.meisterfuu.animexx.objects.SingleValueObjects.ENSSignatureObject;
import static de.meisterfuu.animexx.objects.SingleValueObjects.ENSUnreadObject;
import static de.meisterfuu.animexx.objects.SingleValueObjects.Empty;
import static de.meisterfuu.animexx.objects.SingleValueObjects.GCMIdObject;
import static de.meisterfuu.animexx.objects.SingleValueObjects.XMPPAuthObject;

/**
 * Created by Meisterfuu on 28.09.2014.
 */
public interface WebApiInterface {

    public static final String API_ENDPOINT = "https://ws.animexx.de/json";


    //-------------------------------
    //STECKBRIEFE
    //-------------------------------

    @POST("/mitglieder/steckbrief/?img_max_x=800&img_max_y=800&img_quality=85&img_format=jpg")
    @FormUrlEncoded
    ReturnObject<ProfileObject> getProfile(@Field("user_id") long user_id,
                                           @Field("allefotos") int allefotos,
                                           @Field("mit_gb") int mit_gb,
                                           @Field("mit_statistiken") int mit_statistiken,
                                           @Field("mit_selbstbeschreibung") int mit_selbstbeschreibung,
                                           @Field("mit_boxen_infos") int mit_boxen_infos);

    @POST("/mitglieder/steckbrief/?img_max_x=800&img_max_y=800&img_quality=85&img_format=jpg")
    @FormUrlEncoded
    void getProfile(@Field("user_id") long user_id,
                    @Field("allefotos") int allefotos,
                    @Field("mit_gb") int mit_gb,
                    @Field("mit_statistiken") int mit_statistiken,
                    @Field("mit_selbstbeschreibung") int mit_selbstbeschreibung,
                    @Field("mit_boxen_infos") int mit_boxen_infos,
                    Callback<ReturnObject<ProfileObject>> pCallback);


    @POST("/mitglieder/steckbrief_box/")
    @FormUrlEncoded
    void getProfileBox(@Field("user_id") long user_id, @Field("box_id") String box_id, Callback<ReturnObject<ProfileBoxObject>> pCallback);

    @POST("/mitglieder/steckbrief_box/")
    @FormUrlEncoded
    ReturnObject<ProfileBoxObject> getProfileBox(@Field("user_id") long user_id, @Field("box_id") String box_id);

    @POST("/mitglieder/username_autocomplete/")
    @FormUrlEncoded
    void searchUser(@Field("str") String pUsername, Callback<ReturnObject<List<UserObject>>> pCallback);

    @POST("/mitglieder/username_autocomplete/")
    @FormUrlEncoded
    ReturnObject<List<UserObject>> searchUser(@Field("str") String pUsername);


    @POST("/mitglieder/usernames2ids/?get_user_avatar=true")
    @FormUrlEncoded
    void getUserId(@Field("usernames[]") List<String> pUsernames, Callback<ReturnObject<SingleValueObjects.UserListObject>> pCallback);

    @POST("/mitglieder/usernames2ids/?get_user_avatar=true")
    @FormUrlEncoded
    ReturnObject<SingleValueObjects.UserListObject> getUserId(@Field("usernames[]") List<String> pUsernames);


    @POST("/mitglieder/ich/")
    void getMe(Callback<ReturnObject<UserObject>> pCallback);

    @POST("/mitglieder/ich/")
    ReturnObject<UserObject> getMe();


    //-------------------------------
    //GB
    //-------------------------------

    @POST("/mitglieder/gaestebuch_schreiben/")
    @FormUrlEncoded
    ReturnObject<Boolean> sendGBEntry(@Field("user_id") long pUserId,
                                      @Field("text") String pText,
                                      @Field("avatar_id") long pAvatarId);

    @POST("/mitglieder/gaestebuch_schreiben/")
    @FormUrlEncoded
    void sendGBEntry(@Field("user_id") long pUserId,
                     @Field("text") String pText,
                     @Field("avatar_id") long pAvatarId,
                     Callback<ReturnObject<Boolean>> pCallback);

    @POST("/mitglieder/gaestebuch_info/?get_user_avatar=true")
    @FormUrlEncoded
    void getGBInfo(@Field("user_id") long pUserId, Callback<ReturnObject<GBInfoObject>> pCallback);

    @POST("/mitglieder/gaestebuch_info/?get_user_avatar=true")
    @FormUrlEncoded
    ReturnObject<GBInfoObject> getGBInfo(@Field("user_id") long pUserId);

    @POST("/mitglieder/gaestebuch_lesen/?text_format=both&anzahl=30")
    @FormUrlEncoded
    void getGBEntries(@Field("user_id") long pUserId, @Field("seite") int pPage, Callback<ReturnObject<GBListObject>> pCallback);

    @POST("/mitglieder/gaestebuch_lesen/?text_format=both&anzahl=30")
    @FormUrlEncoded
    ReturnObject<GBListObject> getGBEntries(@Field("user_id") long pUserId, @Field("seite") int pPage);


    //-------------------------------
    //ENS
    //-------------------------------


    @POST("/ens/signatur_get/")
    ReturnObject<ENSSignatureObject> getENSSignature();

    @POST("/ens/signatur_get/")
    void getENSSignature(Callback<ReturnObject<ENSSignatureObject>> pCallback);

    @POST("/ens/signatur_set/")
    @FormUrlEncoded
    ReturnObject<ENSSignatureObject> setENSSignature(@Field("text") String pSignature);


    @POST("/ens/signatur_set/")
    @FormUrlEncoded
    void setENSSignature(@Field("text") String pSignature, Callback<ReturnObject<ENSSignatureObject>> pCallback);


    @POST("/ens/anzahl_ungelesen/")
    ReturnObject<ENSUnreadObject> getENSUnreadCount();

    @POST("/ens/anzahl_ungelesen/")
    void getENSUnreadCount(Callback<ReturnObject<ENSUnreadObject>> pCallback);

    @POST("/ens/anzahl_neue_ens/")
    @FormUrlEncoded
    ReturnObject<ENSUnreadObject> getENSNewCount(@Field("sekunden") int pSeconds);

    @POST("/ens/anzahl_neue_ens/")
    @FormUrlEncoded
    void getENSNewCount(@Field("sekunden") int pSeconds, Callback<ReturnObject<ENSUnreadObject>> pCallback);


    @POST("/ens/ordner_liste/")
    ReturnObject<ENSFolderObject.ENSFolderObjectContainer> getENSFolder();

    @POST("/ens/ordner_liste/")
    void getENSFolder(Callback<ReturnObject<ENSFolderObject.ENSFolderObjectContainer>> pCallback);


    @POST("/ens/ordner_ens_liste/?get_user_avatar=true")
    @FormUrlEncoded
    ReturnObject<List<ENSObject>> getENSList(@Field("ordner_id") long pFolderId, @Field("ordner_typ") String pFolderType, @Field("seite") int pPage);

    @POST("/ens/ordner_ens_liste/?get_user_avatar=true")
    @FormUrlEncoded
    void getENSList(@Field("ordner_id") long pFolderId, @Field("ordner_typ") String pFolderType, @Field("seite") int pPage, Callback<ReturnObject<List<ENSObject>>> pCallback);

    @POST("/ens/ordner_ens_liste/?get_user_avatar=true")
    @FormUrlEncoded
    ReturnObject<List<ENSObject>> getENSList(@Field("ordner_id") long pFolderId, @Field("ordner_typ") String pFolderType, @Field("seite") int pPage, @Field("anzahl") int pQuantity);

    @POST("/ens/ordner_ens_liste/?get_user_avatar=true")
    @FormUrlEncoded
    void getENSList(@Field("ordner_id") long pFolderId, @Field("ordner_typ") String pFolderType, @Field("seite") int pPage, @Field("anzahl") int pQuantity, Callback<ReturnObject<List<ENSObject>>> pCallback);


    @POST("/ens/ens_open/?text_format=html&get_user_avatar=true")
    @FormUrlEncoded
    ReturnObject<ENSObject> getENS(@Field("ens_id") long pId);

    @POST("/ens/ens_open/?text_format=html&get_user_avatar=true")
    @FormUrlEncoded
    void getENS(@Field("ens_id") long pId, Callback<ReturnObject<ENSObject>> pCallback);


    @POST("/ens/an_check/")
    @FormUrlEncoded
    ReturnObject<ENSCheckRecipientsObject> checkENSRecipients(@Field("users[]") List<String> pUserNames);

    @POST("/ens/an_check/")
    @FormUrlEncoded
    void checkENSRecipients(@Field("users[]") List<String> pUserNames, Callback<ReturnObject<ENSCheckRecipientsObject>> pCallback);


    @POST("/ens/ens_senden/")
    @FormUrlEncoded
    ReturnObject<Long> sendENS(@Field("betreff") String pSubject, @Field("text") String pBody, @Field("sig") String pSignature, @Field("an_users[]") List<Long> pUserIds);

    @POST("/ens/ens_senden/")
    @FormUrlEncoded
    void sendENS(@Field("betreff") String pSubject, @Field("text") String pBody, @Field("sig") String pSignature, @Field("an_users[]") List<Long> pUserIds, Callback<ReturnObject<Long>> pCallback);

    @POST("/ens/ens_senden/")
    @FormUrlEncoded
    ReturnObject<Long> sendENS(@Field("betreff") String pSubject, @Field("text") String pBody, @Field("sig") String pSignature, @Field("an_users[]") List<Long> pUserIds, @Field("referenz_typ") String pReferenceType, @Field("referenz_id") long pReferenceId);

    @POST("/ens/ens_senden/")
    @FormUrlEncoded
    void sendENS(@Field("betreff") String pSubject, @Field("text") String pBody, @Field("sig") String pSignature, @Field("an_users[]") List<Long> pUserIds, @Field("referenz_typ") String pReferenceType, @Field("referenz_id") long pReferenceId, Callback<ReturnObject<Long>> pCallback);


    //-------------------------------
    //PERS START
    //-------------------------------

    @POST("/persstart5/get_widget_kommentare/?get_user_avatar=true")
    @FormUrlEncoded
    ReturnObject<HomeCommentObject> getWidgetComments(@Field("widget_id") String pWidget, @Field("item_id") long pItemId);

    @POST("/persstart5/get_widget_kommentare/?get_user_avatar=true")
    @FormUrlEncoded
    void getWidgetComments(@Field("widget_id") String pWidget, @Field("item_id") long pItemId, Callback<ReturnObject<HomeCommentObject>> pCallback);


    @POST("/persstart5/post_item_kommentar/")
    @FormUrlEncoded
    ReturnObject<HomeCommentObject> sendWidgetComment(@Field("widget_id") String pWidget, @Field("item_id") long pItemId, @Field("text") String pComment);

    @POST("/persstart5/post_item_kommentar/")
    @FormUrlEncoded
    void sendWidgetComment(@Field("widget_id") String pWidget, @Field("item_id") long pItemId, @Field("text") String pComment, Callback<ReturnObject<HomeCommentObject>> pCallback);


    //Contact Widget Start
    @POST("/persstart5/get_item_data/?get_user_avatar=true&img_max_x=800&img_max_y=800&img_quality=90&img_format=jpg&widget_id=kontakte")
    @FormUrlEncoded
    ReturnObject<ContactHomeObject> getContectWidgetItem(@Field("item_id") long pItemId, @Field("kommentare") int pComments);

    @POST("/persstart5/get_item_data/?get_user_avatar=true&img_max_x=800&img_max_y=800&img_quality=90&img_format=jpg&widget_id=kontakte")
    @FormUrlEncoded
    void getContactWidgetItem(@Field("item_id") long pItemId, @Field("kommentare") int pComments, Callback<ReturnObject<ContactHomeObject>> pCallback);


    @POST("/persstart5/get_widget_data/?get_user_avatar=true&img_max_x=800&img_max_y=800&img_quality=90&img_format=jpg&widget_id=kontakte")
    ReturnObject<List<ContactHomeObject>> getContectWidget();

    @POST("/persstart5/get_widget_data/?get_user_avatar=true&img_max_x=800&img_max_y=800&img_quality=90&img_format=jpg&widget_id=kontakte")
    void getContactWidget(Callback<ReturnObject<List<ContactHomeObject>>> pCallback);

    @POST("/persstart5/get_widget_data/?get_user_avatar=true&img_max_x=800&img_max_y=800&img_quality=90&img_format=jpg&widget_id=kontakte")
    @FormUrlEncoded
    ReturnObject<List<ContactHomeObject>> getContectWidget(@Field("zeit_von") long pFrom);

    @POST("/persstart5/get_widget_data/?get_user_avatar=true&img_max_x=800&img_max_y=800&img_quality=90&img_format=jpg&widget_id=kontakte")
    @FormUrlEncoded
    void getContactWidget(@Field("zeit_von") long pFrom, Callback<ReturnObject<List<ContactHomeObject>>> pCallback);

    @POST("/persstart5/get_widget_data/?get_user_avatar=true&img_max_x=800&img_max_y=800&img_quality=90&img_format=jpg&widget_id=kontakte")
    @FormUrlEncoded
    ReturnObject<List<ContactHomeObject>> getContectWidget(@Field("zeit_von") long pFrom, @Field("zeit_bis") long pTo);

    @POST("/persstart5/get_widget_data/?get_user_avatar=true&img_max_x=800&img_max_y=800&img_quality=90&img_format=jpg&widget_id=kontakte&widget_id=kontakte")
    @FormUrlEncoded
    void getContactWidget(@Field("zeit_von") long pFrom, @Field("zeit_bis") long pTo, Callback<ReturnObject<List<ContactHomeObject>>> pCallback);

    @POST("/persstart5/get_widget_data/?get_user_avatar=true&img_max_x=800&img_max_y=800&img_quality=90&img_format=jpg&widget_id=kontakte")
    @FormUrlEncoded
    ReturnObject<List<ContactHomeObject>> getContectWidget(@Field("zeit_von") long pFrom, @Field("zeit_bis") long pTo, @Field("user_id") long pUserId);

    @POST("/persstart5/get_widget_data/?get_user_avatar=true&img_max_x=800&img_max_y=800&img_quality=90&img_format=jpg&widget_id=kontakte&widget_id=kontakte")
    @FormUrlEncoded
    void getContactWidget(@Field("zeit_von") long pFrom, @Field("zeit_bis") long pTo, @Field("user_id") long pUserId, Callback<ReturnObject<List<ContactHomeObject>>> pCallback);
    //Contact Widget End


    //-------------------------------
    //Microblog
    //-------------------------------

    @POST("/persstart5/get_widget_data/?get_user_avatar=true&img_max_x=800&img_max_y=800&img_quality=90&img_format=jpg&widget_id=kontakte_blog")
    ReturnObject<List<ContactHomeObject>> getMicroblogs();

    @POST("/persstart5/get_widget_data/?get_user_avatar=true&img_max_x=800&img_max_y=800&img_quality=90&img_format=jpg&widget_id=kontakte_blog")
    void getMicroblogs(Callback<ReturnObject<List<ContactHomeObject>>> pCallback);


    //Post Microblog Start
    @POST("/persstart5/post_microblog_message/")
    @FormUrlEncoded
    ReturnObject<ContactHomeObject> sendMicroblog(@Field("text") String pText);

    @POST("/persstart5/post_microblog_message/")
    @FormUrlEncoded
    void sendMicroblog(@Field("text") String pText, Callback<ReturnObject<ContactHomeObject>> pCallback);


    @POST("/persstart5/post_microblog_message/")
    @FormUrlEncoded
    ReturnObject<ContactHomeObject> sendMicroblog(@Field("text") String pText, @Field("attach_foto") long pImageId);

    @POST("/persstart5/post_microblog_message/")
    @FormUrlEncoded
    void sendMicroblog(@Field("text") String pText, @Field("attach_foto") long pImageId, Callback<ReturnObject<ContactHomeObject>> pCallback);


    @POST("/persstart5/post_microblog_message/")
    @FormUrlEncoded
    ReturnObject<ContactHomeObject> sendMicroblog(@Field("text") String pText, @Field("anhang_typ") String pAttachType, @Field("anhang_id") String pAttachId);

    @POST("/persstart5/post_microblog_message/")
    @FormUrlEncoded
    void sendMicroblog(@Field("text") String pText, @Field("anhang_typ") String pAttachType, @Field("anhang_id") String pAttachId, Callback<ReturnObject<ContactHomeObject>> pCallback);


    @POST("/persstart5/post_microblog_message/")
    @FormUrlEncoded
    ReturnObject<ContactHomeObject> sendMicroblog(@Field("text") String pText, @Field("anhang_typ") String pAttachType, @Field("anhang_id") String pAttachId, @Field("attach_foto") long pImageId);

    @POST("/persstart5/post_microblog_message/")
    @FormUrlEncoded
    void sendMicroblog(@Field("text") String pText, @Field("anhang_typ") String pAttachType, @Field("anhang_id") String pAttachId, @Field("attach_foto") long pImageId, Callback<ReturnObject<ContactHomeObject>> pCallback);
    //Post Microblog End


    //-------------------------------
    //Events
    //-------------------------------

    @POST("/events/event/grunddaten/")
    @FormUrlEncoded
    ReturnObject<EventObject> getEventBasic(@Field("id") long pEventId);

    @POST("/events/event/grunddaten/")
    @FormUrlEncoded
    void getEventBasic(@Field("id") long pEventId, Callback<ReturnObject<EventObject>> pCallback);

    @POST("/events/event/details/")
    @FormUrlEncoded
    ReturnObject<EventObject> getEvent(@Field("id") long pEventId);

    @POST("/events/event/details/")
    @FormUrlEncoded
    void getEvent(@Field("id") long pEventId, Callback<ReturnObject<EventObject>> pCallback);

    @POST("/events/event/programm/")
    @FormUrlEncoded
    ReturnObject<List<EventRoomProgramObject>> getEventProgram(@Field("id") long pEventId);

    @POST("/events/event/programm/")
    @FormUrlEncoded
    void getEventProgram(@Field("id") long pEventId, Callback<ReturnObject<List<EventRoomProgramObject>>> pCallback);

    @POST("/events/event/get_news/")
    @FormUrlEncoded
    ReturnObject<List<WeblogEntryObject>> getEventNews(@Field("event_id") long pEventId, @Field("limit") int pLimit);

    @POST("/events/event/get_news/")
    @FormUrlEncoded
    void getEventNews(@Field("event_id") long pEventId, @Field("limit") int pLimit, Callback<ReturnObject<List<WeblogEntryObject>>> pCallback);

    @POST("/events/event/startseitenevents/")
    ReturnObject<List<EventObject>> getEventsHome();

    @POST("/events/event/startseitenevents/")
    void getEventsHome(Callback<ReturnObject<List<EventObject>>> pCallback);

    @POST("/events/event/dabei_events/")
    ReturnObject<List<EventObject>> getEventsAttending();

    @POST("/events/event/dabei_events/")
    void getEventsAttending(Callback<ReturnObject<List<EventObject>>> pCallback);

    @POST("/events/event/admin_events/")
    ReturnObject<List<EventObject>> getEventsAtmin();

    @POST("/events/event/admin_events/")
    void getEventsAtmin(Callback<ReturnObject<List<EventObject>>> pCallback);

    @POST("/events/event/beschreibung_get/")
    @FormUrlEncoded
    ReturnObject<EventDescriptionObject> getEventDescription(@Field("event_id") long pEventId, @Field("beschreibung_id") long pPageId);

    @POST("/events/event/beschreibung_get/")
    @FormUrlEncoded
    void getEventDescription(@Field("event_id") long pEventId, @Field("beschreibung_id") long pPageId, Callback<ReturnObject<EventDescriptionObject>> pCallback);

    @POST("/events/event/beschreibung_neu/")
    @FormUrlEncoded
    ReturnObject<Integer> setEventDescription(@Field("event_id") long pEventId, @Field("titel") String pTitle, @Field("text") String pText, @Field("html") int pHTML, @Field("anzeigen") int pDraft);

    @POST("/events/event/beschreibung_neu/")
    @FormUrlEncoded
    void setEventDescription(@Field("event_id") long pEventId, @Field("titel") String pTitle, @Field("text") String pText, @Field("html") int pHTML, @Field("anzeigen") int pDraft, Callback<ReturnObject<Integer>> pCallback);

    @POST("/events/event/beschreibung_edit/")
    @FormUrlEncoded
    ReturnObject<Integer> setEventDescription(@Field("event_id") long pEventId, @Field("beschreibung_id") long pPageId, @Field("titel") String pTitle, @Field("text") String pText, @Field("html") int pHTML, @Field("anzeigen") int pDraft);

    @POST("/events/event/beschreibung_edit/")
    @FormUrlEncoded
    void setEventDescription(@Field("event_id") long pEventId, @Field("beschreibung_id") long pPageId, @Field("titel") String pTitle, @Field("text") String pText, @Field("html") int pHTML, @Field("anzeigen") int pDraft, Callback<ReturnObject<Integer>> pCallback);

    @POST("/events/event/beschreibung_del/")
    @FormUrlEncoded
    ReturnObject<Integer> deleteEventDescription(@Field("event_id") long pEventId, @Field("beschreibung_id") long pPageIdt);

    @POST("/events/event/beschreibung_del/")
    @FormUrlEncoded
    void deleteEventDescription(@Field("event_id") long pEventId, @Field("beschreibung_id") long pPageId, Callback<ReturnObject<Integer>> pCallback);

    @POST("/events/event/dabei_get/")
    @FormUrlEncoded
    ReturnObject<List<EventAttender>> getEventAttender(@Field("event_id") long pEventId);

    @POST("/events/event/dabei_get/")
    @FormUrlEncoded
    void getEventAttender(@Field("event_id") long pEventId, Callback<ReturnObject<List<EventAttender>>> pCallback);


    //-------------------------------
    //RPG
    //-------------------------------

    @POST("/rpg/meine_rpgs/?alles=1&kompakt=1")
    @FormUrlEncoded
    ReturnObject<List<RPGObject>> getRPGList(@Field("beendete") int pFinished, @Field("offset") long pOffset, @Field("limit") long pLimit);

    @POST("/rpg/meine_rpgs/?alles=1&kompakt=1")
    @FormUrlEncoded
    void getRPGList(@Field("beendete") int pFinished, @Field("offset") long pOffset, @Field("limit") long pLimit, Callback<ReturnObject<List<RPGObject>>> pCallback);

    @POST("/rpg/erstelle_rpg/")
    @FormUrlEncoded
    ReturnObject<RPGObject> createRPG(@Field("name") String pName, @Field("thema") int pTopic, @Field("adult") int pAdult, @Field("oeffentlich") int pPublic, @Field("sprache") String pLanguageCode, @Field("beschreibung") String pDescription);

    @POST("/rpg/erstelle_rpg/")
    @FormUrlEncoded
    void createRPG(@Field("name") String pName, @Field("thema") int pTopic, @Field("adult") int pAdult, @Field("oeffentlich") int pPublic, @Field("sprache") String pLanguageCode, @Field("beschreibung") String pDescription, Callback<ReturnObject<RPGObject>> pCallback);


    @POST("/rpg/rpg_details_alles/?get_user_avatar=true")
    @FormUrlEncoded
    ReturnObject<RPGObject> getRPG(@Field("rpg") long pRPGId);

    @POST("/rpg/rpg_details_alles/?get_user_avatar=true")
    @FormUrlEncoded
    void getRPG(@Field("rpg") long pRPGId, Callback<ReturnObject<RPGObject>> pCallback);


    @POST("/rpg/get_postings/?text_format=both")
    @FormUrlEncoded
    ReturnObject<List<RPGPostObject>> getRPGPostings(@Field("rpg") long pRPGId, @Field("from_pos") long pFrom, @Field("limit") long pLimit);

    @POST("/rpg/get_postings/?text_format=both")
    @FormUrlEncoded
    void getRPGPostings(@Field("rpg") long pRPGId, @Field("from_pos") long pFrom, @Field("limit") long pLimit, Callback<ReturnObject<List<RPGPostObject>>> pCallback);


    //Without Metadata
    @POST("/rpg/erstelle_posting/")
    @FormUrlEncoded
    ReturnObject<Integer> sendRPGPosting(@Field("rpg") long pRPGId, @Field("text") String pText, @Field("charakter") long pCharaId, @Field("kursiv") int pItalic, @Field("intime") int pInTime);

    @POST("/rpg/erstelle_posting/")
    @FormUrlEncoded
    void sendRPGPosting(@Field("rpg") long pRPGId, @Field("text") String pText, @Field("charakter") long pCharaId, @Field("kursiv") int pItalic, @Field("intime") int pInTime, Callback<ReturnObject<Integer>> pCallback);

    @POST("/rpg/erstelle_posting/")
    @FormUrlEncoded
    ReturnObject<Integer> sendRPGPosting(@Field("rpg") long pRPGId, @Field("text") String pText, @Field("charakter") long pCharaId, @Field("kursiv") int pItalic, @Field("intime") int pInTime, @Field("avatar") long pAvatarId);

    @POST("/rpg/erstelle_posting/")
    @FormUrlEncoded
    void sendRPGPosting(@Field("rpg") long pRPGId, @Field("text") String pText, @Field("charakter") long pCharaId, @Field("kursiv") int pItalic, @Field("intime") int pInTime, @Field("avatar") long pAvatarId, Callback<ReturnObject<Integer>> pCallback);

    //With Metadata
    @POST("/rpg/erstelle_posting/")
    @FormUrlEncoded
    ReturnObject<Integer> sendRPGPosting(@Field("rpg") long pRPGId, @Field("text") String pText, @Field("charakter") long pCharaId, @Field("kursiv") int pItalic, @Field("intime") int pInTime, @FieldMap Map<String, String> pMetaData);

    @POST("/rpg/erstelle_posting/")
    @FormUrlEncoded
    void sendRPGPosting(@Field("rpg") long pRPGId, @Field("text") String pText, @Field("charakter") long pCharaId, @Field("kursiv") int pItalic, @Field("intime") int pInTime, @FieldMap Map<String, String> pMetaData, Callback<ReturnObject<Integer>> pCallback);

    @POST("/rpg/erstelle_posting/")
    @FormUrlEncoded
    ReturnObject<Integer> sendRPGPosting(@Field("rpg") long pRPGId, @Field("text") String pText, @Field("charakter") long pCharaId, @Field("kursiv") int pItalic, @Field("intime") int pInTime, @Field("avatar") long pAvatarId, @FieldMap Map<String, String> pMetaData);

    @POST("/rpg/erstelle_posting/")
    @FormUrlEncoded
    void sendRPGPosting(@Field("rpg") long pRPGId, @Field("text") String pText, @Field("charakter") long pCharaId, @Field("kursiv") int pItalic, @Field("intime") int pInTime, @Field("avatar") long pAvatarId, @FieldMap Map<String, String> pMetaData, Callback<ReturnObject<Integer>> pCallback);


    //-------------------------------
    //RPG Chara
    //-------------------------------

    @POST("/rpg/erstelle_charakter/")
    @FormUrlEncoded
    ReturnObject<RPGObject.PlayerObject> createRPGChara(@Field("rpg") long pRPGId, @Field("spieler") long pUserId, @Field("status") int pStatus);

    @POST("/rpg/erstelle_charakter/")
    @FormUrlEncoded
    void createRPGChara(@Field("rpg") long pRPGId, @Field("spieler") long pUserId, @Field("status") int pStatus, Callback<ReturnObject<RPGObject.PlayerObject>> pCallback);


    @POST("/rpg/loesche_charakter/")
    @FormUrlEncoded
    ReturnObject<RPGObject.PlayerObject> deleteRPGChara(@Field("rpg") long pRPGId, @Field("charakter") long pCharaId);

    @POST("/rpg/loesche_charakter/")
    @FormUrlEncoded
    void deleteRPGChara(@Field("rpg") long pRPGId, @Field("charakter") long pCharaId, Callback<ReturnObject<RPGObject.PlayerObject>> pCallback);


    @POST("/rpg/charakter_einladung_annehmen/")
    @FormUrlEncoded
    ReturnObject<RPGObject.PlayerObject> acceptRPGCharaInvitation(@Field("rpg") long pRPGId, @Field("charakter") long pCharaId);

    @POST("/rpg/charakter_einladung_annehmen/")
    @FormUrlEncoded
    void acceptRPGCharaInvitation(@Field("rpg") long pRPGId, @Field("charakter") long pCharaId, Callback<ReturnObject<RPGObject.PlayerObject>> pCallback);

    @POST("/rpg/charakter_einladung_ablehnen/")
    @FormUrlEncoded
    ReturnObject<RPGObject.PlayerObject> declineRPGCharaInvitation(@Field("rpg") long pRPGId, @Field("charakter") long pCharaId);

    @POST("/rpg/charakter_einladung_ablehnen/")
    @FormUrlEncoded
    void declineRPGCharaInvitation(@Field("rpg") long pRPGId, @Field("charakter") long pCharaId, Callback<ReturnObject<RPGObject.PlayerObject>> pCallback);


    @POST("/rpg/set_charakter_spieler/")
    @FormUrlEncoded
    ReturnObject<RPGObject.PlayerObject> setRPGPlayer(@Field("rpg") long pRPGId, @Field("charakter") long pCharaId, @Field("spieler") long pUserId, @Field("moderator") int pModerator, @Field("status") int pStatus);

    @POST("/rpg/set_charakter_spieler/")
    @FormUrlEncoded
    void setRPGPlayer(@Field("rpg") long pRPGId, @Field("charakter") long pCharaId, @Field("spieler") long pUserId, @Field("moderator") int pModerator, @Field("status") int pStatus, Callback<ReturnObject<RPGObject.PlayerObject>> pCallback);


    @POST("/rpg/add_charakter_avatar/")
    @Multipart
    ReturnObject<RPGObject.PlayerObject> addRPGAvatar(@Query("rpg") long pRPGId, @Field("charakter") long pCharaId, @Part("file") TypedFile pAvatarFile);

    @POST("/rpg/add_charakter_avatar/")
    @Multipart
    void addRPGAvatar(@Query("rpg") long pRPGId, @Field("charakter") long pCharaId, @Part("file") TypedFile pAvatarFile, Callback<ReturnObject<RPGObject.PlayerObject>> pCallback);


    @POST("/rpg/del_charakter_avatar/")
    @FormUrlEncoded
    ReturnObject<RPGObject.PlayerObject> deleteRPGAvatar(@Field("rpg") long pRPGId, @Field("charakter") long pCharaId, @Field("avatar") long pAvatarId);

    @POST("/rpg/del_charakter_avatar/")
    @FormUrlEncoded
    void deleteRPGAvatar(@Field("rpg") long pRPGId, @Field("charakter") long pCharaId, @Field("avatar") long pAvatarId, Callback<ReturnObject<RPGObject.PlayerObject>> pCallback);


    @POST("/rpg/set_charakter_hauptbild/")
    @Multipart
    ReturnObject<RPGObject.PlayerObject> setRPGCharaImage(@Query("rpg") long pRPGId, @Field("charakter") long pCharaId, @Part("file") TypedFile pAvatarFile);

    @POST("/rpg/set_charakter_hauptbild/")
    @Multipart
    void setRPGCharaImage(@Query("rpg") long pRPGId, @Field("charakter") long pCharaId, @Part("file") TypedFile pAvatarFile, Callback<ReturnObject<RPGObject.PlayerObject>> pCallback);


    @POST("/rpg/del_charakter_hauptbild/")
    @FormUrlEncoded
    ReturnObject<RPGObject.PlayerObject> deleteRPGCharaImage(@Field("rpg") long pRPGId, @Field("charakter") long pCharaId);

    @POST("/rpg/del_charakter_hauptbild/")
    @FormUrlEncoded
    void deleteRPGCharaImage(@Field("rpg") long pRPGId, @Field("charakter") long pCharaId, Callback<ReturnObject<RPGObject.PlayerObject>> pCallback);


    @POST("/rpg/set_charakter_beschreibung/")
    @FormUrlEncoded
    ReturnObject<RPGObject.PlayerObject> setRPGCharaDescription(@Field("rpg") long pRPGId, @Field("charakter") long pCharaId, @Field("name") String pName, @Field("beschreibung") String pDescription, @Field("eigenschaften") String pCharaAttributes);

    @POST("/rpg/set_charakter_beschreibung/")
    @FormUrlEncoded
    void setRPGCharaDescription(@Field("rpg") long pRPGId, @Field("charakter") long pCharaId, @Field("name") String pName, @Field("beschreibung") String pDescription, @Field("eigenschaften") String pCharaAttributes, Callback<ReturnObject<RPGObject.PlayerObject>> pCallback);


    //-------------------------------
    //Kontake
    //-------------------------------

    @POST("/kontakte/get_gruppen/")
    ReturnObject<List<ContactGroupObject>> getContactGroups();

    @POST("/kontakte/get_gruppen/")
    void getContactGroups(Callback<ReturnObject<List<ContactGroupObject>>> pCallback);

    @POST("/kontakte/get_kontakte/?get_user_avatar=true")
    ReturnObject<List<UserObject>> getContacts();

    @POST("/kontakte/get_kontakte/?get_user_avatar=true")
    void getContacts(Callback<ReturnObject<List<UserObject>>> pCallback);

    @POST("/kontakte/get_kontakte/?get_user_avatar=true")
    @FormUrlEncoded
    ReturnObject<List<UserObject>> getContactsByGroup(@Field("gruppe") long pGroup);

    @POST("/kontakte/get_kontakte/?get_user_avatar=true")
    @FormUrlEncoded
    void getContactsByGroup(@Field("gruppe") long pGroup, Callback<ReturnObject<List<UserObject>>> pCallback);


    //-------------------------------
    //Kalendar
    //-------------------------------

    @POST("/kalender/list/?zuord_typ=user_private")
    @FormUrlEncoded
    ReturnObject<CalListObject> getPrivateCalendar(@Field("zuord_id") long pId, @Field("tag") String pDay, @Field("zeitraum") String pInterval, @Field("show_events") int pEvents, @Field("show_gebs") int pBirthdays, @Field("show_zirkel") int pZirkel);

    @POST("/kalender/list/?zuord_typ=user_private")
    @FormUrlEncoded
    void getPrivateCalendar(@Field("zuord_id") long pId, @Field("tag") String pDay, @Field("zeitraum") String pInterval, @Field("show_events") int pEvents, @Field("show_gebs") int pBirthdays, @Field("show_zirkel") int pZirkel, Callback<ReturnObject<CalListObject>> pCallback);

    @POST("/kalender/list/?zuord_typ=event")
    @FormUrlEncoded
    ReturnObject<CalListObject> getEventCalendar(@Field("zuord_id") long pId, @Field("tag") String pDay, @Field("zeitraum") String pInterval);

    @POST("/kalender/list/?zuord_typ=event")
    @FormUrlEncoded
    void getEventCalendar(@Field("zuord_id") long pId, @Field("tag") String pDay, @Field("zeitraum") String pInterval, Callback<ReturnObject<CalListObject>> pCallback);


    //-------------------------------
    //Karotaler
    //-------------------------------

    @POST("/items/kt_statistik/")
    ReturnObject<KarotalerStatsObject> getKarotalerStats();

    @POST("/items/kt_statistik/")
    void getKarotalerStats(Callback<ReturnObject<KarotalerStatsObject>> pCallback);

    @POST("/items/kt_abholen/")
    ReturnObject<Integer> getKarotaler();

    @POST("/items/kt_abholen/")
    void getKarotaler(Callback<ReturnObject<Integer>> pCallback);


    //-------------------------------
    //XMPP
    //-------------------------------

    @POST("/xmpp/get_chat_auth/")
    ReturnObject<XMPPAuthObject> getXMPPAuth();

    @POST("/xmpp/get_chat_auth/")
    void getXMPPAuth(Callback<ReturnObject<XMPPAuthObject>> pCallback);

    @POST("/xmpp/log_user_animexx/")
    @FormUrlEncoded
    ReturnObject<List<XMPPHistoryObject>> getXMPPLog(@Field("user_id") long pUserId, @Field("limit") long pLimit);

    @POST("/xmpp/log_user_animexx/")
    @FormUrlEncoded
    void getXMPPLog(@Field("user_id") long pUserId, @Field("limit") long pLimit, Callback<ReturnObject<List<XMPPHistoryObject>>> pCallback);


    //-------------------------------
    //AIDB/MANGA
    //-------------------------------

    @POST("/aidb/mangas/meine_detailliert/")
    ReturnObject<JsonElement> getMangaOwned();

    @POST("/aidb/mangas/meine_detailliert/")
    void getMangaOwned(Callback<ReturnObject<JsonElement>> pCallback);

    @POST("/aidb/mangas/details/")
    @FormUrlEncoded
    ReturnObject<MangaObject> getMangaDetail(@Field("id") String pMangaId);

    @POST("/aidb/mangas/details/")
    @FormUrlEncoded
    void getMangaDetail(@Field("id") String pMangaId, Callback<ReturnObject<MangaObject>> pCallback);

    @POST("/aidb/mangas/serie/")
    @FormUrlEncoded
    ReturnObject<Object> getMangaSeriesDetail(@Field("serie") String pSeriesId);

    @POST("/aidb/mangas/serie/")
    @FormUrlEncoded
    void getMangaSeriesDetail(@Field("serie") String pSeriesId, Callback<ReturnObject<Object>> pCallback);

    @POST("/aidb/mangas/alle/")
    ReturnObject<Object> getMangaAll();

    @POST("/aidb/mangas/alle/")
    void getMangaAll(Callback<ReturnObject<Object>> pCallback);


    //-------------------------------
    //GCM
    //-------------------------------

    @POST("/cloud2device/registration_id_set/")
    @FormUrlEncoded
    ReturnObject<Integer> setGCMId(@Field("registration_id") String pDeviceToken, @Field("collapse_by_type") int pCollapse);

    @POST("/cloud2device/registration_id_set/")
    @FormUrlEncoded
    void setGCMId(@Field("registration_id") String pDeviceToken, @Field("collapse_by_type") int pCollapse, Callback<ReturnObject<Integer>> pCallback);

    @POST("/cloud2device/registration_id_get/")
    ReturnObject<GCMIdObject> getGCMId();

    @POST("/cloud2device/registration_id_get/")
    void getGCMId(Callback<ReturnObject<GCMIdObject>> pCallback);

    @POST("/cloud2device/registration_id_del/")
    @FormUrlEncoded
    ReturnObject<Empty> deleteGCMId(@Field("registration_ids[]") List<String> pEventse);

    @POST("/cloud2device/registration_id_del/")
    @FormUrlEncoded
    void deleteGCMId(@Field("registration_ids[]") List<String> pEventse,  Callback<ReturnObject<Empty>> pCallback);

    @POST("/cloud2device/set_active_events/")
    @FormUrlEncoded
    ReturnObject<Empty> setGCMEvents(@Field("events[]") List<String> pEventse);

    @POST("/cloud2device/set_active_events/")
    @FormUrlEncoded
    void setGCMEvents(@Field("events[]") List<String> pEvents, Callback<ReturnObject<Empty>> pCallback);


    //-------------------------------
    //Weblogs
    //-------------------------------

    @POST("/weblog/get_eintrag/")
    @FormUrlEncoded
    ReturnObject<WeblogEntryObject> getWeblogEntry(@Field("id") String pWeblogEntrieId);

    @POST("/weblog/get_eintrag/")
    @FormUrlEncoded
    void getWeblogEntry(@Field("id") String pWeblogEntrieId, Callback<ReturnObject<WeblogEntryObject>> pCallback);

    @POST("/weblog/get_kommentare/")
    @FormUrlEncoded
    ReturnObject<WeblogCommentObject> getWeblogComments(@Field("id") String pWeblogEntrieId, @Field("limit") String pLimit, @Field("seite") String pPage);

    @POST("/weblog/get_kommentare/")
    @FormUrlEncoded
    void getWeblogComments(@Field("id") String pWeblogEntrieId, @Field("limit") String pLimit, @Field("seite") String pPage, Callback<ReturnObject<WeblogCommentObject>> pCallback);

    @POST("/weblog/get_eintraege/")
    @FormUrlEncoded
    ReturnObject<List<WeblogEntryObject>> getWeblogEntries(@Field("weblog_id") String pWeblogId, @Field("limit") String pLimit, @Field("offset") String pOffset);

    @POST("/weblog/get_eintraege/")
    @FormUrlEncoded
    void getWeblogEntries(@Field("weblog_id") String pWeblogId, @Field("limit") String pLimit, @Field("offset") String pOffset, Callback<ReturnObject<List<WeblogEntryObject>>> pCallback);

    @POST("/weblog/get_eintraege/")
    @FormUrlEncoded
    ReturnObject<List<WeblogEntryObject>> getWeblogEntries(@Field("weblog_id") String pWeblogId, @Field("seit") String pSince);

    @POST("/weblog/get_eintraege/")
    @FormUrlEncoded
    void getWeblogEntries(@Field("weblog_id") String pWeblogId, @Field("seit") String pSince, Callback<ReturnObject<List<WeblogEntryObject>>> pCallback);

    @POST("/weblog/post_eintrag/")
    @FormUrlEncoded
    ReturnObject<Long> createWeblogEntry(@Field("weblog_id") long pWeblogId, @Field("titel") String pTitle, @Field("text") String pText, @Field("entwurf") int pDraft, @Field("html") int pHtml, @Field("adult") int pAdult, @Field("tags") String pTags);

    @POST("/weblog/post_eintrag/")
    @FormUrlEncoded
    void createWeblogEntry(@Field("weblog_id") long pWeblogId, @Field("titel") String pTitle, @Field("text") String pText, @Field("entwurf") int pDraft, @Field("html") int pHtml, @Field("adult") int pAdult, @Field("tags") String pTags, Callback<ReturnObject<Long>> pCallback);

    @POST("/weblog/post_eintrag/")
    @FormUrlEncoded
    ReturnObject<Long> createWeblogEntry(@Field("weblog_id") long pWeblogId, @Field("zuordnung_event") long pEventId, @Field("titel") String pTitle, @Field("text") String pText, @Field("entwurf") int pDraft, @Field("html") int pHtml, @Field("adult") int pAdult, @Field("tags") String pTags);

    @POST("/weblog/post_eintrag/")
    @FormUrlEncoded
    void createWeblogEntry(@Field("weblog_id") long pWeblogId, @Field("zuordnung_event") long pEventId, @Field("titel") String pTitle, @Field("text") String pText, @Field("entwurf") int pDraft, @Field("html") int pHtml, @Field("adult") int pAdult, @Field("tags") String pTags, Callback<ReturnObject<Long>> pCallback);


    @POST("/weblog/del_eintrag/")
    @FormUrlEncoded
    ReturnObject<Long> deleteWeblogEntry(@Field("weblog_id") long pWeblogId, @Field("eintrag_id") long pEntryId);

    @POST("/weblog/del_eintrag/")
    @FormUrlEncoded
    void deleteWeblogEntry(@Field("weblog_id") long pWeblogId, @Field("eintrag_id") long pEntryId, Callback<ReturnObject<Long>> pCallback);


    //-------------------------------
    //Fotoreihen
    //-------------------------------

    @POST("/fotos/reihe/get_reihen_by_mitglied/")
    @FormUrlEncoded
    ReturnObject<PhotoSeriesObject.PhotoSeriesListObject> getPhotoSeriesByUser(@Field("mitglied") String pUserId, @Field("offset") int pOffset, @Field("limit") int pLimit);

    @POST("/fotos/reihe/get_reihen_by_mitglied/")
    @FormUrlEncoded
    void getPhotoSeriesByUser(@Field("mitglied") String pUserId, @Field("offset") int pOffset, @Field("limit") int pLimit, Callback<ReturnObject<PhotoSeriesObject.PhotoSeriesListObject>> pCallback);

    @POST("/fotos/reihe/get_reihen_by_event/")
    @FormUrlEncoded
    ReturnObject<PhotoSeriesObject.PhotoSeriesListObject> getPhotoSeriesByEvent(@Field("event") String pEventId, @Field("offset") int pOffset, @Field("limit") int pLimit);

    @POST("/fotos/reihe/get_reihen_by_event/")
    @FormUrlEncoded
    void getPhotoSeriesByEvent(@Field("event") String pEventId, @Field("offset") int pOffset, @Field("limit") int pLimit, Callback<ReturnObject<PhotoSeriesObject.PhotoSeriesListObject>> pCallback);

    @POST("/fotos/reihe/get_daten/?img_max_x=800&img_max_y=800&img_quality=90&img_format=jpg")
    @FormUrlEncoded
    ReturnObject<PhotoSeriesObject> getPhotoSeries(@Field("reihe") String pId);

    @POST("/fotos/reihe/get_daten/?img_max_x=800&img_max_y=800&img_quality=90&img_format=jpg")
    @FormUrlEncoded
    void getPhotoSeries(@Field("reihe") String pId, Callback<ReturnObject<PhotoSeriesObject>> pCallback);


    //-------------------------------
    //Files
    //-------------------------------

    @POST("/mitglieder/files/list/?img_max_x=800&img_max_y=800&img_quality=90&img_format=jpg")
    ReturnObject<List<UploadedFile>> getFileList();

    @POST("/mitglieder/files/list/?img_max_x=800&img_max_y=800&img_quality=90&img_format=jpg")
    void getFileList(Callback<ReturnObject<List<UploadedFile>>> pCallback);

    @POST("/mitglieder/files/list/?img_max_x=800&img_max_y=800&img_quality=90&img_format=jpg")
    @FormUrlEncoded
    ReturnObject<List<UploadedFile>> getFileList(@Field("filename") String pFilename);

    @POST("/mitglieder/files/list/?img_max_x=800&img_max_y=800&img_quality=90&img_format=jpg")
    @FormUrlEncoded
    void getFileList(@Field("filename") String pFilename, Callback<ReturnObject<List<UploadedFile>>> pCallback);

    @POST("/mitglieder/files/upload/?img_max_x=100&img_max_y=100&img_quality=80&img_format=jpg&thumbnail=1")
    @Multipart
    ReturnObject<FileUploadReturnObject> uploadFile(@Query("filename") String pFilename, @Part("file") TypedFile pFile);

    @POST("/mitglieder/files/upload/?img_max_x=100&img_max_y=100&img_quality=80&img_format=jpg&thumbnail=1")
    @Multipart
    void uploadFile(@Query("filename") String pFilename, @Part("file") TypedFile pFile,Callback<ReturnObject<FileUploadReturnObject>> pCallback);
}



