#ifndef GAMEINVARIANTS_H_INCLUDED
#define GAMEINVARIANTS_H_INCLUDED


#include "..\Utilities\TypeUtils.h"
// TODO: may be a DLL?

//class GameInvariants {
//    public:
//        // TODO: some may become dinamic variables!
//
//        //---------------------------
//        // ---- File Extensions ----
//        //---------------------------
//
//        static const char* FRAMES_EXTENSION;
//        static const char* GOODIES_EXTENSION;
//        static const char* WEAPONS_EXTENSION;
//        static const char* SOUND_EXTENSION;
//        static const char* CHARACTER_EXTENSION;
//        static const char* PALLET_EXTENSION;
//        static const char* LANGUAGE_EXTENSION;
//
//
//
//        //-----------------------
//        // ---- Directories ----
//        //-----------------------
//
//        static const char* EDITORS_WORKING_DIR;
//
//        static const char* ANIMATIONS_DIR;
//        static const char* GOODIES_DIR;
//        static const char* WEAPONS_DIR;
//        static const char* AUDIO_DIR;
//        static const char* CHARACTERS_DIR;
//        static const char* PALLETS_DIR;
//        static const char* LANGUAGE_DIR;
//
//
//        static const char* UNDEFINED;   // TODO: what is this?
//
//
//
//        //---------------------------
//        // ---- Game properties ----
//        //---------------------------
//
//        static const UInt RANDOM_TB_SIZE;
//        static const UByte RANDOM_MAX_VAL;
//        static const UByte LIFE_BAR;
//        static const float VEL_DIVIDER;
//        static const float AIR_CONTROL;
//        static const float ELASTICITY;  // when bouncing against walls
//
//        static const UByte RUN_TIME;
//        static const float GRAVITY;
//        //static const float LOW_GRAVITY;
//
//        //--------------------------------
//        // ---- Default action types ----
//        //--------------------------------
//
//
//        /** Action Types (hit, fire, etc) */
//        static const UByte AT_KNOCK;
//        static const UByte AT_ELECTROCUTION;
//        static const UByte AT_BOMB;
//        static const UByte AT_PEPPER;
//        static const UByte AT_FIRE;
//        static const UByte AT_BULLET;
//        static const UByte AT_THROW;
//
//
//
//
//        //----------------------------------------
//        // ---- Default Character Animations ----
//        //----------------------------------------
//
//        //______________________________
//        // --- involuntary stances ----
//
//        static const UByte INTRODUCING;
//        static const UByte INTRODUCING_END;
//        static const UByte NORMAL;
//        static const UByte WAITING;
//        static const UByte CROUCHING_DOWN;
//        static const UByte LYING;
//        static const UByte STANDING_UP;
//
//        static const UByte IN_AIR;
//        static const UByte LAND;
//
//        static const UByte BEING_GRABBED_FRONT;
//        static const UByte BEING_GRABBED_BACK;
//
//        static const UByte BEING_HIT;
//        static const UByte BEING_GRABBED_HIT;
//        static const UByte BEING_KNOCKED;
//        static const UByte BEING_THROWN;
//        static const UByte SHOCK_IN_WALL_FRONT;
//        static const UByte SHOCK_IN_WALL_BACK;
//        static const UByte BEING_ELECTROCUTTED;
//        static const UByte BEING_BURNED;
//
//
//        static const UByte GRABBING_FRONT;
//        static const UByte GRABBING_BACK;
//
//
//
//        //_______________________
//        // --- Action Moves ----
//
//        // normal moves:
//
//        static const UByte WALK;
//        static const UByte RUN;
//        static const UByte JUMP;
//        static const UByte RUN_JUMP;
//        static const UByte ROLL_UP;
//        static const UByte ROLL_DOWN;
//
//        // if some not defined, the first one is used
//        static const UByte ATTACK_1;
//        static const UByte ATTACK_2;
//        static const UByte ATTACK_3;
//        static const UByte ATTACK_4;
//        static const UByte ATTACK_5;
//
//        static const UByte RUN_ATTACK_STAR_0;
//        static const UByte RUN_ATTACK_STAR_1;
//        static const UByte RUN_ATTACK_STAR_2;
//        static const UByte RUN_ATTACK_STAR_3;
//
//        static const UByte JUMP_STATIC_ATTACK;
//        static const UByte JUMP_MOVE_ATTACK;
//        static const UByte JUMP_RUN_ATTACK;
//        static const UByte JUMP_DOWN_ATTACK;
//
//        static const UByte DEFENCIVE_SPECIAL;
//        static const UByte OFENCIVE_SPECIAL;
//        static const UByte BLOCK;
//
//        static const UByte BACK_ATTACK;
//        static const UByte KNOCK_ATTACK;
//
//        static const UByte SUPPER;
//        static const UByte HELP_CALL;
//
//        static const UByte WEAPON_ATTACK;
//        static const UByte WEAPON_THROW;
//
//
//        // grabbing moves:
//
//        // if some not defined, the first one is used
//        static const UByte FRONT_GRAB_FRONT_ATTACK_1;
//        static const UByte FRONT_GRAB_FRONT_ATTACK_2;
//        static const UByte FRONT_GRAB_FRONT_ATTACK_3;
//        static const UByte FRONT_GRAB_FRONT_ATTACK_4;
//        static const UByte FRONT_GRAB_FRONT_ATTACK_5;
//
//        static const UByte FRONT_GRAB_STATIC_ATTACK;
//        static const UByte FRONT_GRAB_BACK_ATTACK;
//
//        static const UByte BACK_GRAB_FRONT_ATTACK;
//        static const UByte BACK_GRAB_STATIC_ATTACK;
//        static const UByte BACK_GRAB_BACK_ATTACK;
//
//        static const UByte GRAB_WALK;
//        static const UByte GRAB_RUN;
//        static const UByte GRAB_JUMP;
//
//        static const UByte GRAB_RUN_ATTACK;
//        static const UByte FRONT_GRAB_JUMP_ATTACK;
//        static const UByte BACK_GRAB_JUMP_ATTACK;
//
//        static const UByte FLIP_OVER;
//        static const UByte FAILED_FLIP_OVER;
//        static const UByte FLIP_OVER_ATTACK;
//
//        static const UByte BEING_BACK_GRABBED_DEFENCE;
//        static const UByte THROWS_WHOS_GRABBING;
//
//
//        // team moves:
//
//        static const UByte TEAM_FLIP_OVER_ATTACK;
//
//        static const UByte TEAM_FRONT_GRAB_STATIC_ATTACK;
//        static const UByte TEAM_BACK_GRAB_STATIC_ATTACK;
//
//
//
//
//};
//









namespace GameInvariants{

extern const char* FRAMES_EXTENSION;
extern const char* GOODIES_EXTENSION;
extern const char* WEAPONS_EXTENSION;
extern const char* SOUND_EXTENSION;
extern const char* MUSIC_EXTENSION;
extern const char* CHARACTER_EXTENSION;
extern const char* PALLET_EXTENSION;
extern const char* LANGUAGE_EXTENSION;



//-------------------------
// ---- File Versions ----
//-------------------------

extern const UByte FRAMES_VERSION;
extern const UByte GOODIES_VERSION;
extern const UByte WEAPONS_VERSION;
extern const UByte SOUND_VERSION;
extern const UByte MUSIC_VERSION;
extern const UByte CHARACTER_VERSION;
extern const UByte PALLET_VERSION;
extern const UByte LANGUAGE_VERSION;


//-----------------------
// ---- Directories ----
//-----------------------

extern const char* ANIMATIONS_DIR;
extern const char* GOODIES_DIR;
extern const char* WEAPONS_DIR;
extern const char* AUDIO_DIR;
extern const char* CHARACTERS_DIR;
extern const char* PALLETS_DIR;
extern const char* LANGUAGE_DIR;


extern const char* UNDEFINED;



//----------------------
// ---- Game Flags ----
//----------------------

extern const bool FACE_WHEN_HIT;


//---------------------------
// ---- Game properties ----
//---------------------------

extern const UInt RANDOM_TB_SIZE;
extern const UByte RANDOM_MAX_VAL;
extern const UByte LIFE_BAR;
extern const float VEL_DIVIDER;
extern const float ROT_VEL_DIVIDER;
extern const float VERTICAL_VEL_FACTOR;
extern const float AIR_CONTROL;
extern const float  ELASTICITY;

extern const UByte RUN_TIME;
extern const UByte ATTACK_TIME;
extern const UByte KNOCK_TIME;

extern const UByte GRAB_X_SPACE;
extern const UByte GRAB_Y_SPACE;
//extern const UByte SUPER_TIME;

extern const float GRAVITY;
/** when is it considered to being in Z down */
extern const float FALLING_LIMIAR;

/** flip attack can only be made between two frame limits */
extern const float FLIP_ATTACK_MINIMUM;
extern const float FLIP_ATTACK_MAXIMUM;
extern const float JUMP_SPECIAL_MAX_SPEED;
//extern const float LOW_GRAVITY = -0.1f;

/** how many frames the character stops when hiting */
extern const UByte HIT_PAUSE;
extern const UByte KNOCK_PAUSE;
/** time to ignore a hit in the same sircunstancies*/
extern const UByte RECOVER_TIME;
extern const UByte UNGRAB_TIME;

extern const float GROUND_BOUNCE_VEL;
extern const float KNOKED_X_VEL;
extern const float KNOKED_Z_VEL;

extern const float BLOCK_HIT_VEL;
extern const UByte BLOCK_HIT_TIME;




//-------------------------------------------
// ---- Default object and action types ----
//-------------------------------------------

/** Action Types (hit, fire, etc) */
extern const UByte AT_KNOCK;
extern const UByte AT_ELECTROCUTION;
extern const UByte AT_BOMB;
extern const UByte AT_PEPPER;
extern const UByte AT_FIRE;
extern const UByte AT_BULLET;
extern const UByte AT_THROW;







//----------------------------------------
// ---- Default Character Animations ----
//----------------------------------------

//______________________________
// --- involuntary stances ----

extern const UByte INTRODUCING;
extern const UByte INTRODUCING_END;
extern const UByte NORMAL;
extern const UByte WAITING;
extern const UByte CROUCHING_DOWN;
extern const UByte LYING;
extern const UByte STANDING_UP;

extern const UByte IN_AIR;
extern const UByte LAND;

extern const UByte BEING_GRABBED_FRONT;
extern const UByte BEING_GRABBED_BACK;
extern const UByte ATOMIC_FALLING;
extern const UByte BEING_HIT;
extern const UByte BEING_GRABBED_HIT_FRONT;
extern const UByte BEING_GRABBED_HIT_BACK;
extern const UByte BEING_KNOCKED;
extern const UByte BEING_THROWN;
extern const UByte SHOCK_IN_WALL_FRONT;
extern const UByte SHOCK_IN_WALL_BACK;
extern const UByte BEING_ELECTROCUTTED;
extern const UByte BEING_BURNED;


extern const UByte GRABBING_FRONT;
extern const UByte GRABBING_BACK;



//_______________________
// --- Action Moves ----

// normal moves:

extern const UByte WALK;
extern const UByte RUN;
extern const UByte PREPARE_JUMP;
extern const UByte JUMP;
extern const UByte RUN_JUMP;
extern const UByte ROLL_UP;
extern const UByte ROLL_DOWN;

// if some not defined, the first one is used
extern const UByte ATTACK_1;
extern const UByte ATTACK_2;
extern const UByte ATTACK_3;
extern const UByte ATTACK_4;
extern const UByte ATTACK_5;

extern const UByte RUN_ATTACK_STAR_0;
extern const UByte RUN_ATTACK_STAR_1;
extern const UByte RUN_ATTACK_STAR_2;
extern const UByte RUN_ATTACK_STAR_3;

extern const UByte JUMP_STATIC_ATTACK;
extern const UByte JUMP_MOVE_ATTACK;
extern const UByte JUMP_RUN_ATTACK;
extern const UByte JUMP_DOWN_ATTACK;

extern const UByte DEFENCIVE_SPECIAL;
extern const UByte OFENCIVE_SPECIAL;
extern const UByte JUMP_SPECIAL;
extern const UByte BLOCK;

extern const UByte BACK_ATTACK;
extern const UByte KNOCK_ATTACK;

extern const UByte SUPPER;
extern const UByte HELP_CALL;

extern const UByte WEAPON_ATTACK;
extern const UByte WEAPON_THROW;


// grabbing moves:

// if some not defined, the first one is used
extern const UByte FRONT_GRAB_FRONT_ATTACK_1;
extern const UByte FRONT_GRAB_FRONT_ATTACK_2;
extern const UByte FRONT_GRAB_FRONT_ATTACK_3;
extern const UByte FRONT_GRAB_FRONT_ATTACK_4;
extern const UByte FRONT_GRAB_FRONT_ATTACK_5;

extern const UByte FRONT_GRAB_STATIC_ATTACK;
extern const UByte FRONT_GRAB_BACK_ATTACK;

extern const UByte BACK_GRAB_FRONT_ATTACK;
extern const UByte BACK_GRAB_STATIC_ATTACK;
extern const UByte BACK_GRAB_BACK_ATTACK;
extern const UByte GRAB_WALK;
extern const UByte GRAB_BACK_WALK;
extern const UByte GRAB_RUN;
extern const UByte GRAB_JUMP;

extern const UByte GRAB_RUN_ATTACK;
extern const UByte FRONT_GRAB_JUMP_ATTACK;
extern const UByte BACK_GRAB_JUMP_ATTACK;

extern const UByte FLIP_OVER;
extern const UByte FAILED_FLIP_OVER;
extern const UByte FLIP_OVER_ATTACK;

extern const UByte BEING_BACK_GRABBED_DEFENCE;
extern const UByte THROWS_WHOS_GRABBING;


// team moves:

extern const UByte TEAM_FLIP_OVER_ATTACK;
extern const UByte TEAM_FRONT_GRAB_STATIC_ATTACK;
extern const UByte TEAM_BACK_GRAB_STATIC_ATTACK;

// mixelaneous:
extern const UByte DYING;
extern const UByte GRABBING_UP;
extern const UByte FRONT_GRAB_JUMP_ATTACK_LAND;
extern const UByte BACK_GRAB_JUMP_ATTACK_LAND;

extern const UByte GROUND_BOUNCING;
extern const UByte SAFE_LANDING;

}



#endif // GAMEINVARIANTS_H_INCLUDED
