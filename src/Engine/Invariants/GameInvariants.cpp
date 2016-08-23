#include "GameInvariants.h"


const char* GameInvariants::FRAMES_EXTENSION = "icl";
const char* GameInvariants::GOODIES_EXTENSION = "gst";
const char* GameInvariants::WEAPONS_EXTENSION = "wpn";
const char* GameInvariants::SOUND_EXTENSION = "se";
const char* GameInvariants::MUSIC_EXTENSION = "mu";
const char* GameInvariants::CHARACTER_EXTENSION = "ch";
const char* GameInvariants::PALLET_EXTENSION = "pll";
const char* GameInvariants::LANGUAGE_EXTENSION = "lang";




//-------------------------
// ---- File Versions ----
//-------------------------

const UByte GameInvariants::FRAMES_VERSION      = 1;
const UByte GameInvariants::GOODIES_VERSION     = 1;
const UByte GameInvariants::WEAPONS_VERSION     = 2;
const UByte GameInvariants::SOUND_VERSION       = 1;
const UByte GameInvariants::MUSIC_VERSION       = 1;
const UByte GameInvariants::CHARACTER_VERSION   = 3;
const UByte GameInvariants::PALLET_VERSION      = 1;
const UByte GameInvariants::LANGUAGE_VERSION    = 1;




//-----------------------
// ---- Directories ----
//-----------------------

const char* GameInvariants::ANIMATIONS_DIR = "animations\\";
const char* GameInvariants::GOODIES_DIR = "goodies\\";
const char* GameInvariants::WEAPONS_DIR = "weapons\\";
const char* GameInvariants::AUDIO_DIR = "audio\\";
const char* GameInvariants::CHARACTERS_DIR = "characters\\";
const char* GameInvariants::PALLETS_DIR = "pallets\\";
const char* GameInvariants::LANGUAGE_DIR = "language\\";


const char* GameInvariants::UNDEFINED = "undefined";



//----------------------
// ---- Game Flags ----
//----------------------

const bool GameInvariants::FACE_WHEN_HIT = false;


//---------------------------
// ---- Game properties ----
//---------------------------
const UInt GameInvariants::RANDOM_TB_SIZE = 512;
const UByte GameInvariants::RANDOM_MAX_VAL = 255;
const UByte GameInvariants::LIFE_BAR = 50;
const float GameInvariants::VEL_DIVIDER = 10.f;
const float GameInvariants::ROT_VEL_DIVIDER = 4.f;
const float GameInvariants::VERTICAL_VEL_FACTOR = 0.75f;
const float GameInvariants::AIR_CONTROL = 0.5; //0.2;
const float  GameInvariants::ELASTICITY = 0.5;

const UByte GameInvariants::RUN_TIME    = 20;
const UByte GameInvariants::ATTACK_TIME = 25;
const UByte GameInvariants::KNOCK_TIME  = 40;

const UByte GameInvariants::GRAB_X_SPACE = 24;
const UByte GameInvariants::GRAB_Y_SPACE = 15;

const float GameInvariants::GRAVITY = -0.48f;
const float GameInvariants::FALLING_LIMIAR = 1.5f;
const float GameInvariants::FLIP_ATTACK_MINIMUM = 0.25;
const float GameInvariants::FLIP_ATTACK_MAXIMUM = 0.75;
const float GameInvariants::JUMP_SPECIAL_MAX_SPEED = 3.;
//const float GameInvariants::LOW_GRAVITY = -0.1f;


const UByte GameInvariants::HIT_PAUSE = 4;
const UByte GameInvariants::KNOCK_PAUSE = 5;
const UByte GameInvariants::RECOVER_TIME = 15;
const UByte GameInvariants::UNGRAB_TIME = 25;

const float GameInvariants::GROUND_BOUNCE_VEL = 2;
const float GameInvariants::KNOKED_X_VEL = 3.5;
const float GameInvariants::KNOKED_Z_VEL = 8;

const float GameInvariants::BLOCK_HIT_VEL = -2.5;
const UByte GameInvariants::BLOCK_HIT_TIME = 5;




//-------------------------------------------
// ---- Default object and action types ----
//-------------------------------------------

/** Action Types (hit, fire, etc) */
const UByte GameInvariants::AT_KNOCK = 128;
const UByte GameInvariants::AT_ELECTROCUTION = 50;
const UByte GameInvariants::AT_BOMB = 51;
const UByte GameInvariants::AT_PEPPER = 52;
const UByte GameInvariants::AT_FIRE = 53;
const UByte GameInvariants::AT_BULLET = 54;
const UByte GameInvariants::AT_THROW = 60;







//----------------------------------------
// ---- Default Character Animations ----
//----------------------------------------

//______________________________
// --- involuntary stances ----

const UByte GameInvariants::INTRODUCING     = 0;
const UByte GameInvariants::INTRODUCING_END = 1;
const UByte GameInvariants::NORMAL          = 2;
const UByte GameInvariants::WAITING         = 3;
const UByte GameInvariants::CROUCHING_DOWN  = 4;
const UByte GameInvariants::LYING           = 5;
const UByte GameInvariants::STANDING_UP     = 6;

const UByte GameInvariants::IN_AIR          = 8;
const UByte GameInvariants::LAND            = 9;

const UByte GameInvariants::BEING_GRABBED_FRONT = 11;
const UByte GameInvariants::BEING_GRABBED_BACK  = 12;
const UByte GameInvariants::ATOMIC_FALLING      = 13;
const UByte GameInvariants::BEING_HIT           = 14;
const UByte GameInvariants::BEING_GRABBED_HIT_FRONT  = 15;
const UByte GameInvariants::BEING_GRABBED_HIT_BACK   = 16;
const UByte GameInvariants::BEING_KNOCKED       = 17;
const UByte GameInvariants::BEING_THROWN        = 18;
const UByte GameInvariants::SHOCK_IN_WALL_FRONT = 19;
const UByte GameInvariants::SHOCK_IN_WALL_BACK  = 20;
const UByte GameInvariants::BEING_ELECTROCUTTED = 21;
const UByte GameInvariants::BEING_BURNED        = 22;


const UByte GameInvariants::GRABBING_FRONT      = 23;
const UByte GameInvariants::GRABBING_BACK       = 24;



//_______________________
// --- Action Moves ----

// normal moves:

const UByte GameInvariants::WALK                = 27;
const UByte GameInvariants::RUN                 = 28;
const UByte GameInvariants::PREPARE_JUMP        = 29;
const UByte GameInvariants::JUMP                = 30;
const UByte GameInvariants::RUN_JUMP            = 31;
const UByte GameInvariants::ROLL_UP             = 32;
const UByte GameInvariants::ROLL_DOWN           = 33;

// if some not defined, the first one is used
const UByte GameInvariants::ATTACK_1            = 35;
const UByte GameInvariants::ATTACK_2            = 36;
const UByte GameInvariants::ATTACK_3            = 37;
const UByte GameInvariants::ATTACK_4            = 38;
const UByte GameInvariants::ATTACK_5            = 39;

const UByte GameInvariants::RUN_ATTACK_STAR_0   = 41;
const UByte GameInvariants::RUN_ATTACK_STAR_1   = 42;
const UByte GameInvariants::RUN_ATTACK_STAR_2   = 43;
const UByte GameInvariants::RUN_ATTACK_STAR_3   = 44;

const UByte GameInvariants::JUMP_STATIC_ATTACK  = 46;
const UByte GameInvariants::JUMP_MOVE_ATTACK    = 47;
const UByte GameInvariants::JUMP_RUN_ATTACK     = 48;
const UByte GameInvariants::JUMP_DOWN_ATTACK    = 49;

const UByte GameInvariants::DEFENCIVE_SPECIAL   = 51;
const UByte GameInvariants::OFENCIVE_SPECIAL    = 52;
const UByte GameInvariants::JUMP_SPECIAL        = 53;
const UByte GameInvariants::BLOCK               = 54;

const UByte GameInvariants::BACK_ATTACK         = 55;
const UByte GameInvariants::KNOCK_ATTACK        = 56;

const UByte GameInvariants::SUPPER              = 58;
const UByte GameInvariants::HELP_CALL           = 59;

const UByte GameInvariants::WEAPON_ATTACK       = 61;
const UByte GameInvariants::WEAPON_THROW        = 62;


// grabbing moves:

// if some not defined, the first one is used
const UByte GameInvariants::FRONT_GRAB_FRONT_ATTACK_1 = 65;
const UByte GameInvariants::FRONT_GRAB_FRONT_ATTACK_2 = 66;
const UByte GameInvariants::FRONT_GRAB_FRONT_ATTACK_3 = 67;
const UByte GameInvariants::FRONT_GRAB_FRONT_ATTACK_4 = 68;
const UByte GameInvariants::FRONT_GRAB_FRONT_ATTACK_5 = 69;

const UByte GameInvariants::FRONT_GRAB_STATIC_ATTACK  = 71;
const UByte GameInvariants::FRONT_GRAB_BACK_ATTACK    = 72;

const UByte GameInvariants::BACK_GRAB_FRONT_ATTACK  = 74;
const UByte GameInvariants::BACK_GRAB_STATIC_ATTACK = 75;
const UByte GameInvariants::BACK_GRAB_BACK_ATTACK   = 76;
const UByte GameInvariants::GRAB_WALK               = 77;
const UByte GameInvariants::GRAB_BACK_WALK          = 78;
const UByte GameInvariants::GRAB_RUN                = 79;
const UByte GameInvariants::GRAB_JUMP               = 80;

const UByte GameInvariants::GRAB_RUN_ATTACK         = 82;
const UByte GameInvariants::FRONT_GRAB_JUMP_ATTACK  = 83;
const UByte GameInvariants::BACK_GRAB_JUMP_ATTACK   = 84;

const UByte GameInvariants::FLIP_OVER               = 86;
const UByte GameInvariants::FAILED_FLIP_OVER        = 87;
const UByte GameInvariants::FLIP_OVER_ATTACK        = 88;

const UByte GameInvariants::BEING_BACK_GRABBED_DEFENCE = 90;
const UByte GameInvariants::THROWS_WHOS_GRABBING       = 91;


// team moves:

const UByte GameInvariants::TEAM_FLIP_OVER_ATTACK   = 94;
const UByte GameInvariants::TEAM_FRONT_GRAB_STATIC_ATTACK  = 95;
const UByte GameInvariants::TEAM_BACK_GRAB_STATIC_ATTACK   = 96;

// mixelaneous
const UByte GameInvariants::DYING = 98;
const UByte GameInvariants::GRABBING_UP = 99;
const UByte GameInvariants::FRONT_GRAB_JUMP_ATTACK_LAND = 100;
const UByte GameInvariants::BACK_GRAB_JUMP_ATTACK_LAND = 101;

const UByte GameInvariants::GROUND_BOUNCING = 103;
const UByte GameInvariants::SAFE_LANDING = 104;
