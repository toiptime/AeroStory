/*Vote point exchange npc
Exchanges votepoints for white scrolls dragon weapons and reverse weapons.
@@author shadowzzz*/
var status;
function start() {
status = -1;//sets status to -1
action( 1, 0, 0);
}
function action (mode, type , selection) {
if (mode == 1) { 
     status++; 
 }else{ 
       status--; 
}
if (status == 0) { 
cm.sendSimple("Welcome to the vote point exchange npc you have.#r" +cm.getvotePoints() +"#k Votepoints Go to the website and vote to gain votepoints. What would u like to buy with your votepoints? #d\r\n#L0# Buy White scrolls 3-6 votepoints #b\r\n#L1# Buy Dragon weapons 5 votepoints #r\r\n#L2# Buy Reverse Weapons 10 votepoints ");
}else if (status == 1){
if (selection == 0) {
cm.sendSimple("#eHow many whitescrolls would u like? #r\r\n#L100# 1 White scroll for 3 votepoints #b\r\n\#L101# 5 White scrolls for 15 points");
}else if (selection== 1){
cm.sendSimple("#b\r\n#L102# Dragon carabella#b\r\n\#L103# Dragon Axe#b\r\n\#L104# Dragon Mace#b\r\n\#L105# Dragon Wand#b\r\n\#L106# Dragon Staff #b\r\n\#L107# Dragon Kanzir #b\r\n\#L108#Dragon Claymore #b\r\n\#L109# Dragon battleaxe #b\r\n\#L110# Dragon flame #b\r\n\#L111# Dragon faltizan #b\r\n\#L112# Dragon Chelbrid #b\r\n\#L113# Dragon shinerbow #b\r\n\#L114# Dragon ShinerCrossbow #b\r\n\#L115# Dragon Purple Sleeve #b\r\n\#L116# Dragon Slash Claw #b\r\n\#L117# Dragonfire Revolver");
}else if (selection == 2){
cm.sendSimple("#b\r\n\#L118# Reverse Executioners #b\r\n\#L119# Reverse Bardiche #b\r\n\#L120# Reverse Allergando #b\r\n\#L121# Reverse Pescas #b\r\n\#L122# Reverse Killic #b\r\n\#L123# Reverse EnrealTear #b\r\n\#L124# Reverse Aeas Hand #b\r\n\#L125# Reverse Neibelheim #b\r\n\#L126# Reverse Tabarzin #b\r\n\#L127# Reverse Bellocce #b\r\n\#L128# Reverse Alchupiz #b\r\n\#L129# Reverse Diesra #b\r\n\#L130# Reverse Engaw #b\r\n\#L131# Reverse Black Beauty #b\r\n\#L132# Reverse Lampion #b\r\n\#L133# Reverse Equinox #b\r\n\#L134# Reverse Blindness");
     } 
}else if (status == 2){
if (selection == 100){
if (cm.getvotePoints()>= 3){
cm.gainItem(2340000, 1);
cm.gainvotePoints(-3);
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection == 101){
if (cm.getvotePoints() >= 6){
cm.gainItem(2340000, 5);
cm.gainvotePoints(-15);
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection == 102){
if (cm.getvotePoints() >= 5){
cm.gainItem(1302059, 1);
cm.gainvotePoints(-5);
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 103){
if (cm.getvotePoints() >= 5){
cm.gainItem(1312031, 1);
cm.gainvotePoints(-5)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 104){
if (cm.getvotePoints() >= 5){
cm.gainItem(1322052, 1);
cm.gainvotePoints(-5)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 105){
if (cm.getvotePoints() >= 5){
cm.gainItem(1372032, 1);
cm.gainvotePoints(-5)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 106){
if (cm.getvotePoints() >= 5){
cm.gainItem(1382036, 1);
cm.gainvotePoints(-5)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 107){
if (cm.getvotePoints() >= 5){
cm.gainItem(1332049, 1);
cm.gainvotePoints(-5)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 108){
if (cm.getvotePoints() >= 5){
cm.gainItem(1402036, 1);
cm.gainvotePoints(-5)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 109){
if (cm.getvotePoints() >= 5){
cm.gainItem(1412026, 1);
cm.gainvotePoints(-5)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 110){
if (cm.getvotePoints() >= 5){
cm.gainItem(1422028, 1);
cm.gainvotePoints(-5)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 111){
if (cm.getvotePoints() >= 5){
cm.gainItem(1432038, 1);
cm.gainvotePoints(-5)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 112){
if (cm.getvotePoints() >= 5){
cm.gainItem(1442045, 1);
cm.gainvotePoints(-5)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 113){
if (cm.getvotePoints() >= 5){
cm.gainItem(1452044, 1);
cm.gainvotePoints(-5)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 114){
if (cm.getvotePoints() >= 5){
cm.gainItem(1462039, 1);
cm.gainvotePoints(-5)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 115){
if (cm.getvotePoints() >= 5){
cm.gainItem(1472052, 1);
cm.gainvotePoints(-5)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 116){
if (cm.getvotePoints() >= 5){
cm.gainItem(1482013, 1);
cm.gainvotePoints(-5)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 117){
if (cm.getvotePoints() >= 5){
cm.gainItem(1492013, 1);
cm.gainvotePoints(-5)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 118){
if (cm.getvotePoints() >= 10){
cm.gainItem(01302086, 1);
cm.gainvotePoints(-10)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 119){
if (cm.getvotePoints() >= 10){
cm.gainItem(01312038, 1);
cm.gainvotePoints(-10)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 120){
if (cm.getvotePoints() >= 10){
cm.gainItem(01322061, 1);
cm.gainvotePoints(-10)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 121){
if (cm.getvotePoints() >= 10){
cm.gainItem(1332075, 1);
cm.gainvotePoints(-10)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 122){
if (cm.getvotePoints() >= 10){
cm.gainItem(1332076, 1);
cm.gainvotePoints(-10)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 123){
if (cm.getvotePoints() >= 10){
cm.gainItem(1372045, 1);
cm.gainvotePoints(-10)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 124){
if (cm.getvotePoints() >= 10){
cm.gainItem(1382059, 1);
cm.gainvotePoints(-10)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 125){
if (cm.getvotePoints() >= 10){
cm.gainItem(1402047, 1);
cm.gainvotePoints(-10)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 126){
if (cm.getvotePoints() >= 10){
cm.gainItem(1412034, 1);
cm.gainvotePoints(-10)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 127){
if (cm.getvotePoints() >= 10){
cm.gainItem(1422038, 1);
cm.gainvotePoints(-10)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 128){
if (cm.getvotePoints() >= 10){
cm.gainItem(1432049, 1);
cm.gainvotePoints(-10)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 129){
if (cm.getvotePoints() >= 10){
cm.gainItem(1442067, 1);
cm.gainvotePoints(-10)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 130){
if (cm.getvotePoints() >= 10){
cm.gainItem(1452059, 1);
cm.gainvotePoints(-10)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 131){
if (cm.getvotePoints() >= 10){
cm.gainItem(1462051, 1);
cm.gainvotePoints(-10)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 132){
if (cm.getvotePoints() >= 10){
cm.gainItem(1472071, 1);
cm.gainvotePoints(-10)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 133){
if (cm.getvotePoints() >= 10){
cm.gainItem(1482024, 1);
cm.gainvotePoints(-10)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
}
}else if (selection  == 134){
if (cm.getvotePoints() >= 10){
cm.gainItem(1492025, 1);
cm.gainvotePoints(-10)
cm.dispose();
}else{
cm.sendOk("you dont have enough vote points!");
cm.dispose();
        }
     }
   }
 }