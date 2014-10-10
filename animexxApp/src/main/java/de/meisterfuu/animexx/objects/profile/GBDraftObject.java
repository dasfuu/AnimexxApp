package de.meisterfuu.animexx.objects.profile;

import de.meisterfuu.animexx.objects.UserObject;

/**
 * Created by Furuha on 25.07.2014.
 */
public class GBDraftObject {

//	- user_id: int / benötigt
//	@DatabaseField(dataType = DataType.SERIALIZABLE)
	private UserObject recipient;

//	- text: string / benötigt
//	@DatabaseField
	private String text;

//	- avatar_id: int / optional / die ID des zu verwendenden Avatars
//	@DatabaseField
	private int avatar;

	public GBDraftObject() {
		avatar = -1;
	}

	public UserObject getRecipient() {
		return recipient;
	}

	public void setRecipient(final UserObject pRecipient) {
		recipient = pRecipient;
	}

	public String getText() {
		return text;
	}

	public void setText(final String pText) {
		text = pText;
	}

	public int getAvatar() {
		return avatar;
	}

	public void setAvatar(final int pAvatar) {
		avatar = pAvatar;
	}
}
