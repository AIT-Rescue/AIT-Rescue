package comlib.message;

import rescuecore2.standard.entities.Human;
import rescuecore2.worldmodel.EntityID;


public abstract class MessageHuman extends MessageInformation
{

	protected int rawHumanID;
	protected int rawHumanPosition;
	protected EntityID humanID;
	protected int humanHP;
	protected int humanBuriedness;
	protected int humanDamage;
	protected EntityID humanPosition;

	public MessageHuman(int messageID, Human human)
	{
		super(messageID);
		humanID = human.getID();
		humanHP = human.getHP();
		humanBuriedness = human.getBuriedness();
		humanDamage = human.getDamage();
		humanPosition = human.getPosition();
	}

	public MessageHuman(int messageID, int time, int ttl, int hp, int buriedness, int damage, int position, int id)
	{
		super(messageID, time, ttl);
		humanHP = hp;
		humanBuriedness = buriedness;
		humanDamage = damage;
		rawHumanPosition = position;
		rawHumanID = id;
	}

	public EntityID getHumanID()
	{
		if (this.humanID == null)
		{ this.humanID = new EntityID(this.rawHumanID); }
		return this.humanID;
	}

	public int getHP() { return this.humanHP; }

	public int getBuriedness() { return this.humanBuriedness; }

	public int getDamage() { return this.humanDamage; }

	public EntityID getPosition()
	{
		if (this.humanPosition == null)
		{ this.humanPosition = new EntityID(this.rawHumanPosition); }
		return this.humanPosition;
	}
}

