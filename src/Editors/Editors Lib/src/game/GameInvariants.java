/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package game;

import weapons.Weapon;
import goodies.Goodie;
import util.UBPoint;

/**
 *
 * @author Gil Costa
 */
public class GameInvariants {
    
    //---------------------------
    // ---- File Extensions ----
    //---------------------------
    
    public static final String FRAMES_EXTENSION = "icl";
    public static final String GOODIES_EXTENSION = "gst";
    public static final String WEAPONS_EXTENSION = "wpn";
    public static final String SOUND_EXTENSION = "se";
    public static final String MUSIC_EXTENSION = "mu";
    public static final String CHARACTER_EXTENSION = "ch";
    public static final String PALLET_EXTENSION = "pll";
    public static final String LANGUAGE_EXTENSION = "lang";
    
    
    //-------------------------
    // ---- File Versions ----
    //-------------------------
    
    public static final int FRAMES_VERSION = 1;
    public static final int GOODIES_VERSION = 1;
    public static final int WEAPONS_VERSION = 2;
    public static final int SOUND_VERSION = 1;
    public static final int MUSIC_VERSION = 1;
    public static final int CHARACTER_VERSION = 3;
    public static final int PALLET_VERSION = 1;
    public static final int LANGUAGE_VERSION = 1;
    
    
    
    //-----------------------
    // ---- Directories ----
    //-----------------------
    
    public static final String EDITORS_WORKING_DIR = "";
    
    public static final String ANIMATIONS_DIR = "animations";
    public static final String GOODIES_DIR = "goodies";
    public static final String WEAPONS_DIR = "weapons";
    public static final String AUDIO_DIR = "audio";
    public static final String CHARACTERS_DIR = "characters";
    public static final String PALLETS_DIR = "pallets";
    public static final String LANGUAGE_DIR = "language";
    
    
    public static final String UNDEFINED = "-------------- ND --------------";
    public static final String SPECIFIC = "user defined";
   
    
    
    //-------------------------------------------
    // ---- Default object and action types ----
    //-------------------------------------------
    
    public static final int DEFAULT_LIFE_BAR = 50;
    
    
    
    /** Character stance types */
    public static final String[] stanceTypes = {
        //________________________________
        // --- "involuntary" stances ----

        "introducing",
        "introduction end",
        "normal",
        "waiting",
        "crouching down",
        "lying",
        "standing up",
        UNDEFINED,
        "in hair",
        "land",
        UNDEFINED,
        "being grabbed front",
        "being grabbed back",
        "on atomic falling",
        "being hit",
        "being front grabbed hit",
        "being back grabbed hit",
        "being knocked",
        "being thrown",
        "shock in wall front",
        "shock in wall back",
        "being electrocuted",
        "being burned",
        //UNDEFINED,
        "grabbing front",
        "grabbing back",
        UNDEFINED,
        UNDEFINED,


        //_______________________
        // --- Action Moves ----

        // normal moves:

        "walk",
        "run",
        "prepare to jump",
        "jump",
        "run jump",
        "roll up",
        "roll down",
        UNDEFINED,

        // if some not defined, they're skiped
        // uses 2 counters, one for the # of the move, other to reset the first counter after a while of inactivity
        // reaching the KNOCK_ATTACK automatically resets both counters.
        // Also any other move other than walk and run resets the counters (not as in sor3)
        "attack 1",
        "attack 2",
        "attack 3",
        "attack 4",
        "attack 5",
        UNDEFINED,
        // accordingly to the number of stars
        "run attack Star 0",
        "run attack Star 1",
        "run attack Star 2",
        "run attack Star 3",
        UNDEFINED,
        "jump static attack",
        "jump move attack",
        "jump run attack",
        "jump down attack",
        UNDEFINED,
        "defencive special",
        "ofencive special",
        "jump special",
        "block",
        "back attack",
        "knock attack",
        UNDEFINED,
        "supper",
        "help call",
        UNDEFINED,

        "weapon attack",
        //note: I sugest that D becomes only to the back attack,
        // sometimes we want to denfend ourselfs and has no time to throw the weapon away
        "weapon throw",
        UNDEFINED,
        UNDEFINED,

        // grabbing moves:

        // if some not defined, they're skiped
        "front grab front attack 1",
        "front grab front attack 2",
        "front grab front attack 3",
        "front grab front attack 4",
        "front grab front attack 5",
        UNDEFINED,
        "front grab static attack",
        "front grab back attack",
        UNDEFINED,
        "back grab front attack",
        "back grab static attack",
        "back grab back attack",
        "grabbing walk",
        "grabbing walk backwards",
        "grabbing run",
        "grabbbing jump",
        UNDEFINED,
        "grabbing run attack",
        "front grab jump attack",
        "back grab jump attack",
        UNDEFINED,
        "flip over",
        "failed flip over",
        "flip over attack",
        UNDEFINED,
        "being back grabbed defence",
        "throws who's grabbing",
        UNDEFINED,
        UNDEFINED,


        // team moves:

        "team flip over attack",
        "team front grab static attack",
        "team back grab static attack",
        UNDEFINED,
        "dying",
        "grabbing up",
        "landing front grab jump attack",
        "landing back grab jump attack",
        UNDEFINED,
        "ground bouncing",
        "safe landing",
        UNDEFINED,
        UNDEFINED,
        UNDEFINED,
        UNDEFINED,
        UNDEFINED,
        UNDEFINED,
        SPECIFIC+" 1", SPECIFIC+" 2", SPECIFIC+" 3", SPECIFIC+" 4", SPECIFIC+" 5", SPECIFIC+" 6", SPECIFIC+" 7", SPECIFIC+" 8", SPECIFIC+" 9", SPECIFIC+" 10",
        SPECIFIC+" 11", SPECIFIC+" 12", SPECIFIC+" 13", SPECIFIC+" 14", SPECIFIC+" 15", SPECIFIC+" 16", SPECIFIC+" 17", SPECIFIC+" 18", SPECIFIC+" 19", SPECIFIC+" 20",
        SPECIFIC+" 21", SPECIFIC+" 22", SPECIFIC+" 23", SPECIFIC+" 24", SPECIFIC+" 25", SPECIFIC+" 26", SPECIFIC+" 27", SPECIFIC+" 28", SPECIFIC+" 29", SPECIFIC+" 30",
        SPECIFIC+" 31", SPECIFIC+" 32", SPECIFIC+" 33", SPECIFIC+" 34", SPECIFIC+" 35", SPECIFIC+" 36", SPECIFIC+" 37", SPECIFIC+" 38", SPECIFIC+" 39", SPECIFIC+" 40",
        SPECIFIC+" 41", SPECIFIC+" 42", SPECIFIC+" 43", SPECIFIC+" 44", SPECIFIC+" 45", SPECIFIC+" 46", SPECIFIC+" 47", SPECIFIC+" 48", SPECIFIC+" 49", SPECIFIC+" 50",
        SPECIFIC+" 51", SPECIFIC+" 52", SPECIFIC+" 53", SPECIFIC+" 54", SPECIFIC+" 55", SPECIFIC+" 56", SPECIFIC+" 57", SPECIFIC+" 58", SPECIFIC+" 59", SPECIFIC+" 60",
        SPECIFIC+" 61", SPECIFIC+" 62", SPECIFIC+" 63", SPECIFIC+" 64", SPECIFIC+" 65", SPECIFIC+" 66", SPECIFIC+" 67", SPECIFIC+" 68", SPECIFIC+" 69", SPECIFIC+" 70",
        SPECIFIC+" 71", SPECIFIC+" 72", SPECIFIC+" 73", SPECIFIC+" 74", SPECIFIC+" 75", SPECIFIC+" 76", SPECIFIC+" 77", SPECIFIC+" 78", SPECIFIC+" 79", SPECIFIC+" 80",
        SPECIFIC+" 81", SPECIFIC+" 82", SPECIFIC+" 83", SPECIFIC+" 84", SPECIFIC+" 85", SPECIFIC+" 86", SPECIFIC+" 87", SPECIFIC+" 88", SPECIFIC+" 89", SPECIFIC+" 90",
        SPECIFIC+" 91", SPECIFIC+" 92", SPECIFIC+" 93", SPECIFIC+" 94", SPECIFIC+" 95", SPECIFIC+" 96", SPECIFIC+" 97", SPECIFIC+" 98", SPECIFIC+" 99", SPECIFIC+" 100",
        
        
        
    };
    
    
    
    /** Goodies types */
    public static final String[] goodieTypes = {
        "Food", "Points", "Lives", "Help Calls", "Supers"
    };
    
    
    
    /** Action Types (hit, fire, etc) */
    public static final int AT_KNOCK = 128;
    public static final int AT_ELECTROCUTION = 50;
    public static final int AT_BOMB = 51;
    public static final int AT_PEPPER = 52;
    public static final int AT_FIRE = 53;
    public static final int AT_BULLET = 54;
    public static final int AT_THROW = 60;
    
    
    public static final String actionType(int b){
        if (b>=AT_KNOCK){
            String s = actionType(b-AT_KNOCK);
            if (s == null) return null;
            return "knowck " + s;
        }
        if (b>0 && b<AT_BOMB) return "Hit " + b;
        switch(b){
            case AT_ELECTROCUTION: return "electrocution";
            case AT_BOMB: return "bomb";
            case AT_PEPPER: return "pepper";
            case AT_FIRE: return "fire";
            case AT_BULLET: return "bullet";
            case AT_THROW: return "Throw";
        }
        return null; 
    }
    
    
    
    public static final Goodie[] defaultGoodies = {
        new Goodie(0,0,16,GOODIES_VERSION), new Goodie(1,0,DEFAULT_LIFE_BAR,GOODIES_VERSION),
        new Goodie(2,1,1000,GOODIES_VERSION), new Goodie(3,1,5000, GOODIES_VERSION),
        new Goodie(4,2,1, GOODIES_VERSION), new Goodie(5,3,1, GOODIES_VERSION),
        new Goodie(6,4,1, GOODIES_VERSION)
    };
    
    //public static final String[] defaultWeapons = {
    //    "knife", "Kunail", "Lead Pipe", "Plank", "Baseball Bat", "Sword", "bomb", "Pepper Shaker", "Botle", "Cracked Botle"
    //};
    
    public static final Weapon[] defaultWeapons = {
        new Weapon(0,6 + AT_KNOCK, new UBPoint[]{new UBPoint(18,1)},false, WEAPONS_VERSION),
        new Weapon(1,7 + AT_KNOCK, new UBPoint[]{new UBPoint(20,2)},false, WEAPONS_VERSION),
        new Weapon(2,8 + AT_KNOCK, new UBPoint[]{new UBPoint(50,3),new UBPoint(25,3)},true, WEAPONS_VERSION),
        new Weapon(3,8 + AT_KNOCK, new UBPoint[]{new UBPoint(50,3),new UBPoint(25,3)},true, WEAPONS_VERSION),
        new Weapon(4,8 + AT_KNOCK, new UBPoint[]{new UBPoint(50,3),new UBPoint(25,3)},true, WEAPONS_VERSION),
        new Weapon(5,10 + AT_KNOCK, new UBPoint[]{new UBPoint(50,3),new UBPoint(25,3)},false, WEAPONS_VERSION),
        new Weapon(6,51, new UBPoint[]{},true, WEAPONS_VERSION),
        new Weapon(7,52, new UBPoint[]{},false, WEAPONS_VERSION),
        new Weapon(8,7 + AT_KNOCK, new UBPoint[]{new UBPoint(19,1)},false, WEAPONS_VERSION),
        new Weapon(9,7 + AT_KNOCK, new UBPoint[]{new UBPoint(19,1)},false, WEAPONS_VERSION),
    };
    
}
