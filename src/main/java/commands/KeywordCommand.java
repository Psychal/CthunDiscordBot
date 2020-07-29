package commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import objects.CommandInterfaceBot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KeywordCommand implements CommandInterfaceBot {
    enum Keywords{
        AUTOCAST("Auto-Cast","Causes the Hero Power to be used automatically at the start of each turn, provided the hero has enough mana to spare."),
        AUTOATTACK("Auto-Attack","Causes the minion to attack automatically at the end of the turn, targeting the minion or minions directly across from it. The minion cannot be commanded to attack, or prevented from attacking."),
        ADAPT("Adapt","Choose from one of three possible upgrades to the possessing minion. "),
        BATTLECRY("Battlecry","Battlecry is a type of triggered effect, which activates when the card is played from your hand."),
        CASTSWHENDRAWN("Casts When Drawn","The spell card is automatically cast for no mana when drawn from your deck, and the next card in the deck is then drawn. Only found on a few Uncollectible spells. "),
        CHARGE("Charge","Enables the minion to attack on the same turn that it is summoned. "),
        CHOOSEONE("Choose One","Gives the controlling player the ability to choose between two or more effects stated in the card. Found only on druid cards. "),
        CHOOSSETWICE("Choose Twice","A variation of Choose One that allows the player to combine two options (or pick the same option twice) from a list. Found only on druid cards. "),
        COMBO("Combo","Triggers an effect if you already played another card this turn. Found only on rogue cards. "),
        COUNTER("Counter","Prevents a spell that was just cast from taking effect. Currently the only card to use Counter is the mage spell Counterspell; see that page for details. "),
        DEATHRATTLE("Deathrattle","Deathrattle is a type of triggered effect, which activates when the minion dies. It is identified by a skull and crossbones that appears at the bottom of the minion's portrait. "),
        DISCOVER("Discover","Choose one card from three random class-appropriate cards, then add it to your hand. "),
        DIVINESHIELD("Divine Shield","Absorbs the first source of damage taken by the minion, removing the shield. "),
        ECHO("Echo","Adds another copy of the card to your hand after it's played that disappears at the end of your turn. "),
        FREEZE("Freeze","Frozen characters lose their next attack. "),
        IMMUNE("Immune","Immune is an ability that prevents damage dealt from any source to the target, and prevents all enemy interaction with the target. "),
        INSPIRE("Inspire","Inspire is a type of triggered effect that activates each time the controlling hero uses their Hero Power. It is identified by a blue flag that appears at the bottom of the minion's portrait. "),
        LIFESTEAL("Lifesteal","Lifesteal is a type of triggered effect, which causes damage dealt by the card to also restore Health to the controlling hero. It is identified by a broken purple heart that appears at the bottom of the minion's portrait. "),
        MAGNETIC("Magnetic","Placing a Magnetic minion to the left of a Mech merges the two cards together, combining their stats and effects. "),
        MEGAWINDFURY("Mega-Windfury","Can attack four times each turn. Mega-Windfury is indicated by multiple wind-like lines around the card. "),
        OVERKILL("Overkill","A bonus is rewarded when dealing more damage than needed to kill its target. "),
        OVERLOAD("Overload","You have {X} less mana next turn. This effect is activated when the card is played from the hand, and typically allows a powerful card to be played for a low mana cost this round, at the price of reduced mana next turn. Found only on shaman cards. "),
        OUTCAST("Outcast","Gains an effect if it's the right-most or left-most card in your hand. Found only on demon hunter cards. "),
        PASSIVE("Passive","Cards that continuously provide a global effect to the game via an aura. Passive hero powers are a main source of this ability. All Passive cards currently known have no mana cost associated with them. "),
        POISONOUS("Poisonous","Any minion damaged by a Poisonous minion is destroyed. A type of triggered effect, Poisonous is identified by a green bubbling flask at the bottom of the minion's portrait. "),
        QUEST("Quest","Similar to Secrets, Quests are a special type of spell card that does not take effect until certain requirements are met, triggering its effect. Unlike Secrets, Quests can be seen by the opponent, and the conditions must be met by the possessing player rather than the opponent. Each deck can include only one Quest, which will be automatically included in the player's mulligan. All Quests are legendary. "),
        RECRUIT("Recruit","Summons a random minion from the player's deck. A keyworded version of Put into battlefield, found only on Kobolds & Catacombs cards with this effect. "),
        RUSH("Rush","Enables the minion to attack enemy minions on the same turn that it is summoned. "),
        SECRET("Secret","A special type of spell card that remains hidden until its trigger condition occurs, revealing it and triggering its effect. Most Secrets can only be activated during the opponent's turn, but some exceptions activate at the start of the player's next turn. Only one of any given Secret can be in play for a given player at a time. "),
        SILENCE("Silence","Removes all card text, enchantments, and abilities from the target minion, except for auras provided by external cards in play. Silence does not prevent enchantments and abilities from being applied to said minion afterwards, however. "),
        SIDEQUEST("Sidequest","A type of spell card that does not take effect until certain requirements are met, granting a Reward upon completion. Unlike Quests, Sidequests do not start in the mulligan. Only one of any given Sidequest can be in play for a given player at a time."),
        STARTOFGAME("Start of Game","A type of triggered effect that activates at the start of the game. "),
        STEALTH("Stealth","Minions with Stealth may not be the target of enemy attacks, spells or abilities until they attack or deal damage. Once they attack or deal damage, Stealth is removed. Minions with Stealth can still be affected by AoE spells such as Consecration, and randomly targeted spells such as Arcane Missiles. "),
        SPELLBURST("Spellburst","A one-time Triggered effect that does something the first time you cast a spell. It is identified by a sparkle that appears at the bottom of the minion's portrait."),
        SPELLDAMAGE("Spell Damage","Increases the output of your damaging spells by {X}. Spell Damage is indicated by blue and purple sparkles rising from the card. "),
        TAUNT("Taunt","Enemies must attack minions with Taunt before any non-Taunt characters. This includes minions and hero attacks. Taunt is illustrated by a large shield-like border. "),
        TWINSPELL("Twinspell","Casting the spell puts another copy into your hand, but without the Twinspell ability. "),
        WINDFURY("Windfury","Can attack twice each turn. Windfury is indicated by multiple wind-like lines around the card. "),
        TOKEN("Token","Token cards are brought onto the board through card effects. In other words a term for most uncollectible cards."),
        ELITE("Elite","Elite cards can be identified by the dragon frame and are more powerful."),
        COLLECTIBLE("Collectible","Collectible cards are all cards that can be added to a player's collection."),
        DEATHKNIGHT("Death Knight","Death Knight spell cards that are generated from The Lich King and Arfus."),
        JADEGOLEM("Jade Golem","Each Jade Golem summoned throughout the match is +1/+1 larger than the last. Found only on Druid, Rogue, Shaman, and Jade Lotus Tri-class cards."),
        LACKEY("Lackey","1/1 minions with useful Battlecries."),
        SPAREPART("Spare Part","1 mana spells with minor benefits."),
        CANTATTACK("Can't Attack", "Prevents a card from attacking."),
        CANTATTACKHERO("Can't Attack Heroes","Prevents a card from attacking a hero."),
        CARDDRAWEFF("Card Draw Effect","Causes the player to draw one or more cards from their deck."),
        CASTSPELL("Cast Spell","Casts a spell on behalf of the controlling player."),
        COPY("Copy","Copies an existing card. Usually combined with a Summon effect to summon the copy into the battlefield, or a generate effect to place it into the player's hand."),
        DEALDAMAGE("Deal Damage","Deals damage to a character, attempting to reduce its Health by the stated amount."),
        DESTROY("Destroy",""),
        DISABLEHEROPOWER("Disable Hero Power","Causes both players to be unable to use their Hero Power. This effect does not work against Adventure- and Mission-specific Hero Powers."),
        DISCARD("Discard","Causes the player to discard one or more cards from their hand."),
        ELUSIVE("Elusive","An unofficial term for minions which cannot be targeted by spells or Hero Powers, from either player. "),
        ENCHANT("Enchant","Modifies the statistics or card text of one or more cards. May be temporary, permanent, or ongoing."),
        ENRAGE("Enrage","While damaged, this minion gains the stated effect. Prior to Patch 11.0.0.23966, Enrage appeared as a keyword, but was replaced with generic text due to its scarcity."),
        EQUIP("Equip","Equips the hero with the stated weapon. "),
        FORGETFUL("Forgetful","A term for a minion which has a 50% chance of attacking the wrong enemy. Forgetful is a type of triggered effect. "),
        GAINARMOR("Gain Armor","Causes the controlling hero to gain Armor. "),
        GENERATE("Generate","Generates a new card, and places it into the player's hand. "),
        INVOKE("Invoke","If a version of Galakrond is in your deck, triggers his hero power and contributes to upgrading him. "),
        INCREMENTATTRIB("Increment Attribute","Adds or subtracts a value from something's Attack, Health, Cost, or Durability. "),
        JOUST("Joust","Reveals a random card from each player's deck. If the player who initiated the Joust produces a card with a higher mana cost, they win the Joust, activating a secondary effect. Both cards are then shuffled back into their respective decks. "),
        MINDCONTROLEFF("Mind Control Effect","Transfers ownership of the targeted minion. "),
        MODIFYCOST("Modify Cost","Alters the mana cost of another card or Hero Power. "),
        MULTIPLYATTRIB("Multiply Attribute","Multiplies something's Attack, Health or Cost by some value, which so far has always been 2. "),
        NODURABLOST("No Durability Lost","Causes the possessing hero's weapon to not lose any Durability during their turn. "),
        PERMANENT("Permanent","Permanent \"minions\" lack Mana cost, Attack and Health, are untargetable, are immune to all effects except their own, do not count as minions for effects, and take up a more or less permanent residence on the battlefield. "),
        PLAYERBOUND("Playerbound","Provides a permanent effect bound to the player. Similar to the aura-generation effect associated with the keyword ability \"Passive\". "),
        PUTINTOBATTLE("Put Into Battlefield","Directly places a card from the player's deck or hand into the battlefield, without triggering any Battlecries. "),
        PUTINTOHAND("Put Into Hand","Draws cards of a specific type directly from the player's deck and places them into the player's hand. "),
        REBORN("Reborn","The first time the minion dies, it returns to life with 1 Health remaining. Reborn is represented by a cracked blue glow around the minion."),
        REFRESHMANA("Refresh Mana","Refills the player's empty Mana Crystals."),
        REMOVEFROMDECK("Remove From Deck","Removes cards directly from the player's deck, without placing them into the hand. Similar to discard effects."),
        REPLACE("Replace","Replace one card with another, without destroying or discarding the original card."),
        RESTOREHEALTH("Restore Health","Heals a character, increasing their Health by the stated amount, up to but not beyond their current maximum Health."),
        RETURN("Return","Returns the target minion to its owner's hand."),
        SETATTRIB("Set Attribute","Via an enchantment, assigns a new value for something's Attack, Health, or Cost."),
        SHRINE("Shrine","Whenever the minion would be destroyed or removed from the field in any way or get Silenced, it instead becomes dormant for 3 turns."),
        SHUFFLEINTODECK("Shuffle Into Deck","Places a card into a player's unused deck, with its placement randomly determined."),
        SPENDMANA("Spend Mana","Spends mana in addition to the normal mana cost to produce an extra effect. Currently, all cards with this ability spend all available mana, and the effect is proportional to the amount of mana spent. "),
        SUMMON("Summon","Summons the specified minion/s. Distinct from simply playing a minion card from the hand, this is an ability found on spell or minion cards, sometimes triggered by a Battlecry or Deathrattle. "),
        TRANSFORM("Transform","Changes a minion into something else irreversibly, entirely replacing the previous card."),
        TRANSFORMINHAND("Transform In Hand","At the start of the controlling player's turn, changes a card into something else while it is in the player's hand. "),
        UNLIMITEDATK("Unlimited Attacks","Allows the possessing hero to attack any number of times each turn.");

        String keyword;
        String desc;

        Keywords(String keyword, String desc) {
            this.keyword = keyword;
            this.desc = desc;
        }
        private static final Map<String, Keywords> keywordsIndex = new HashMap<>(Keywords.values().length);

        static{
            for(Keywords keyword : Keywords.values()){
            keywordsIndex.put(keyword.getKeyword().toLowerCase().replace(" ",""),keyword);
            }
        }

        public static Keywords getIndex(String name){
            return keywordsIndex.get(name);
        }
        public String getKeyword(){
            return keyword;
        }
        public String getDesc(){
            return desc;
        }
    }
    @Override
    public void handle(List<String> args, MessageReceivedEvent event) {
        String keywordInput = String.join("",args).toLowerCase().replace("!thunkeyword","");
        if(keywordInput.equals("all")){
            event.getAuthor().openPrivateChannel().queue((channel)-> channel.sendMessage(keywordListEmbed().build()).queue());
            event.getMessage().addReaction("\u2705").queue();
            return;
        }
        if(keywordInput.equals("")){
            event.getChannel().sendMessage("For a list of all keywords type `!thun keyword all`").queue();
        }
        else if(Keywords.getIndex(keywordInput) == null){
            event.getChannel().sendMessage("Keyword does not exist.").queue();
        }
        else {
            event.getChannel().sendMessage("**"+Keywords.getIndex(keywordInput).getKeyword()+"** - "+Keywords.getIndex(keywordInput).getDesc()).queue();
        }
    }

    private EmbedBuilder keywordListEmbed(){
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("Hearthstone Keywords:");
        StringBuilder description = builder.getDescriptionBuilder();
        for(Keywords keyword : Keywords.values()){
            description.append("`").append(keyword.getKeyword()).append("`\n");
        }
        return builder;
    }

    @Override
    public String getHelp() {
        return "Gives you a description of whatever keyword you're searching for.\n" +
                "Example usage: `"+getInvoke()+" Token`\n" +
                "For a list of all keywords type `!thun keyword all`";
    }

    @Override
    public String getInvoke() {
        return "!thun keyword";
    }
}
