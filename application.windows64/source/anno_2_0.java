import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.io.File; 
import controlP5.*; 
import java.util.regex.*; 
import java.util.regex.MatchResult; 
import java.util.ArrayList; 
import java.util.HashMap; 
import java.util.List; 
import java.util.Map; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class anno_2_0 extends PApplet {











PrintWriter output;
String dir ;

String[]fileNames;
StringList fileNames2 = new StringList();
StringList fileNames2pfad = new StringList();
IntDict rating = new IntDict();

String status = "willkommen"; 
int count = 0;
int zaehler = 0;
String typing = "";
int counti = 0;
int counter = 0;
int counter2 = 0;
String textstring = "";
int textanzahl;
int t1 = 0;
int t2 = 1;

int size = 0;
int size2 = 0;
int wiederholungen = 0;
int wert1;
int wert2;
int wert3;
StringList texte = new StringList();
StringList texte2 = new StringList();
StringList textnamen = new StringList();
StringList anno = new StringList();
IntList reihenfolge_alt = new IntList();

// neue Variablen
StringDict korpus = new StringDict();
File korpusdatei;
File ergebnissdatei;
int start_text = 0;
int end_text = 0;
ControlP5 cp5;
RadioButton r;
boolean regular = false;
boolean counts = false;
IntList reihenfolge = new IntList();
StringDict texto = new StringDict();
IntList annoergebniss = new IntList();
IntList annoergebnisse2 = new IntList();
IntList anno2texte = new IntList();


//kappa variablen
int gemeinsamrelevant = 0;
int a1_1 = 0;
int a1_0 = 0;
int a2_1 = 0;
int a2_0 = 0;
float pa;
float pe;
float kappa;
public void setup() {
  size(1300,800);
  PFont font = createFont("arial",20);
  cp5 = new ControlP5(this);
  output = createWriter("Ergebnisse.txt");
    background(0);
    rect(400,20,500,20);
    rect(400,200,500,20);
    textSize(27);
    text("Anno 2.0",590,100);
    text("Korpus-Annotations-Tool",490,150);
    textSize(15);
    text("Dr\u00fccken sie 'o' und w\u00e4hlen sie die Korpus.txt Datei aus."
    +"\n"+"Die Korpsudatei muss alle Texte enthalten, die zu annotieren sind."
    +"\n"+"Sie werden danach die Anleitung zur Annotation sehen.",450,400);
  
}
 
public void draw() {

  if (status == "Annotation"){
    cp5.hide();
    background(255);
   
    text("TextA",230,150);
    text(korpus.get(str(reihenfolge.get(t1))),60,200);
    text("TextB",950,150);
    text(korpus.get(str(reihenfolge.get(t2))),770,200);
    text("Dr\u00fccken sie 'a' f\u00fcr TextA oder 'b' f\u00fcr TextB",500,70);
    status = "gedr\u00fcckt";
  } 
  if (status == "Ende"){
    background(0);
    //println(rating);
    fill(255);
    textSize(15);
    text("Annotatins-Ende",570,300);
    rating.sortValuesReverse();
    textSize(13);
    text("Dr\u00fccken sie ' f ' um die Annotations zu beenden und die Ergebnisse zu speichern.",430,400);
    status = "speichern";
  }
  if (status =="EndeAnno"){
    background(255);
    fill(0);
    text("Um den Cohens-Kappa Wert mit anderen Ergebnissen zu \u00fcberpr\u00fcfen bitte 'o' dr\u00fccken"
    +"\n"+" und die entsprechende Ergebnissdatei einlesen"
    +"\n"+"\n"+"Um die Annoatation zu beenden dr\u00fccken Sie 'e'."
    +"\n"+"Die Ergebnissdatei finden Sie im entpsrechenden Ordner",500,500);
  }

} 

public void keyPressed() {
  // Status Willkommen
  if ((key == 'o') && (status == "willkommen")){
    selectInput("Korpusdatei ausw\u00e4hlen :", "fileSelected");
    status = "anleitung";    
  }
  if ((key == ' ') && (status == "anleitung")){
    cp5.show();
        // n\u00e4chster Frame kommt :
    background(255);
    
    fill(0);
    textSize(17);
    text("Annotation zur Textqualit\u00e4t",515,50);
    textSize(12);
    text("Anleitung",200,140);
    text("Richtlinien zur Annotation",900,140);
    text("Sie werden immer Textpaare zu sehen bekommen. Einen Text A und"
    +"\n"+"einen Text B. Ihre Aufgabe ist es zu entscheiden, welchen Text Sie f\u00fcr"
    +"\n"+"qualitativ hochwertiger halten. Um Ihre Entscheidung zu best\u00e4tigen,"
    +"\n"+"dr\u00fccken Sie die Taste 'a' f\u00fcr den Text A und 'b' f\u00fcr den Text B.",60,200);
    text("Lesen Sie sich die beiden Texte in Ruhe durch und entscheiden Sie erst dann. Zur leichteren"
    +"\n"+ "Entscheidungsfindung, sollten sie auf Merkmale wie Lesbarkeit und Verst\u00e4ndlichkeit achten.",700,200);

    text("Beispiels\u00e4tze :"+"\n"+"\n"+"'Dieser Tag ist Rudolf.'"
   +"\n"+"Obwohl dieser Satz grammatikalisch korrekt gebildet wurde, ist seine Verst\u00e4ndlichkeit sehr schlecht."
   +"\n"+"\n"+"'Heute ist Freitag. Dar\u00fcber freue ich mich.'"
   +"\n"+"Dieser Satz liefert eine gute Verst\u00e4ndlichkeit. Das Pronominaladverb dar\u00fcber verbindet die beiden" 
   +"\n"+"S\u00e4tze miteinander und tr\u00e4gt positiv zum Verst\u00e4ndnis bei."
   +"\n"+"\n"+"'Ich wei\u00df, dass ich nichts wei\u00df.'"
   +"\n"+"In diesem Satz spielt ein Konnektor eine positive Rolle, im Bezug auf die Verst\u00e4ndlichkeit."
   +"\n"+"\n"+"'Er hat das im Laufe der Jahre stark heruntergekommene Fahrrad, das er damals zur Kommunion"
   +"\n"+"geschenkt bekommen hatte, dem Kind gegeben.'"
   +"\n"+"'Ihr solltet jetzt wegfahren, weil ihr, wenn ihr noch lange wartet, im Stau stehen werdet'"
   +"\n"+"Die beiden S\u00e4tze spiegeln unn\u00f6tig komplizierte Wort- und Satzstellungen wieder. Erschwert die"
   +"\n"+"Lesbarkeit und verringert das Verst\u00e4ndnis."
   +"\n"+"\n"+"'Wer das behauptet, l\u00fcgt. vs Er l\u00fcgt.'"
   +"\n"+"1. Satz komplexer Aufbau, 2. Satz einfacher Aufbau. Der Einfache Aufbau f\u00fchrt zu mehr Lesbarkeit"
   +"\n"+"und zu mehr Verst\u00e4ndniss.",700,250);
  line(700,325,1280,325);
  line(700,400,1280,400);
  line(700,455,1280,455);
  line(700,570,1280,570); 
  line(700,650,1280,650);
  line(60,300,500,300); 
  text("Auswahl des Textbereichs",200,335);
  text("Anzahl der Texte im Korpus: "+(korpus.size()-1),60,380); 
  text("Bitte geben Sie den Bereich der Texte f\u00fcr die Annotation an"
   +"\n"+"und best\u00e4tigen Sie mit ENTER."
   +"\n"+"Beispiel: 1-10 oder 3-5",60,420);
 
      cp5.addTextfield("input")
     .setPosition(60,470)
     .setSize(200,40)
     .setFocus(true)
     .setColor(color(255,0,0))
     ;
//       r = cp5.addRadioButton("radioButton")
//         .setPosition(700,500)
//         .setSize(40,20)
//         .setColorForeground(color(120))
//         .setColorActive(color(100))
//         .setColorLabel(color(100))
//         .setItemsPerRow(2)
//         .setSpacingColumn(50)
//         .addItem("regulaer",1)
//         .addItem("bestimmte Anzahl",2)
//         ;
//    switch(key) {
//    case('0'): r.deactivateAll(); break;
//    case('1'): r.activate(0); break;
//    case('2'): r.activate(1); break;
//    }
   status = "anleitungfertig";
  text("Dr\u00fccken Sie 'l' zum Starten der Annotation",60,700); 
  }
  
  
  // Status Annotation
  
  // reihenfolge der Textpaare wird festgelegt
  if ((key == 'l') && (status == "anleitungfertig")){
    // werte werden neu initialisiert f\u00fcr jeden Durchgang,
    // damit die ben\u00f6tigetn variablen wieder auf 0 gesetzt werden.
    gemeinsamrelevant = 0;
    a1_0 = 0;
    a1_1 = 0;
    a2_0 = 0;
    a2_1 = 0;
    reihenfolge.clear();
    anno.clear();
    rating.clear();
    annoergebniss.clear();
    annoergebnisse2.clear();
    t1=0;
    t2=1;
    count = 0;
    if(start_text == 0 && end_text ==0){
      text("Bitte geben Sie zuerst einen korrekten Bereich im Zahlenfeld an"+
      "\n"+"bevor Sie die Annotation starten",200,570);
     
    }
    else{
      for(int i = start_text; i< end_text;i++){
        wert1 = i;
        for(int u = i+1; u<= end_text;u++){
          //println(i);
          wert2 = u;
          reihenfolge.append(wert1);
          reihenfolge.append(wert2);
        }
      }
      for(int i=0;i<reihenfolge.size();i++){
        println("i."+reihenfolge.get(i));
      }
   // textnamen werden festgelegt
     for(int i = start_text; i<= end_text; i++){
       texto.set(str(i),"Text "+str(i)); 
     }
     println(texto);
  // rating dic wird initialisiert
     for(int i = start_text; i<=end_text;i++){
       rating.set(texto.get(str(i)),0);
     }
     println(rating);
     println(korpus);
     println(korpus.get(str(reihenfolge.get(3))));
      status = "Annotation";
    }
  }
  

  if ((key == 'a') && (status == "gedr\u00fcckt")){
    //println("gedr\u00fcckt");
    count = count +1;
    //println("ccc",count);
    rating.add(texto.get(str(reihenfolge.get(t1))),1);
    anno.append(texto.get(str(reihenfolge.get(t1)))); // a dr\u00fccken =  1. Text wird ausgew\u00e4hlt
    anno.append(texto.get(str(reihenfolge.get(t2))));
    annoergebniss.append(0);
    if(count==(reihenfolge.size()/2)){
      status = "Ende";
    }
    else{
    t1 = t1 +2;
    t2 = t2 +2;
    println("t1 "+t1,"t2 "+t2);
    status = "Annotation";
    }
  }
  
  if ((key == 'b') && (status == "gedr\u00fcckt")){
    //println("gedr\u00fcckt");
    count = count +1;
    //println("ccc",count);
    rating.add(texto.get(str(reihenfolge.get(t2))),1);
    anno.append(texto.get(str(reihenfolge.get(t1))));
    anno.append(texto.get(str(reihenfolge.get(t2)))); // b dr\u00fccken =  2. Text wird ausgw\u00e4hlt
    annoergebniss.append(1);
    if(count==(reihenfolge.size()/2)){
      status = "Ende";
      
    }
    else{
    t1 = t1 +2;
    t2 = t2 +2; 
    println("t1 "+t1,"t2 "+t2);
    status = "Annotation";
    }
  }
  if ((key == 'f') && (status == "speichern")){
    output.println("Annotations-Ergebnisse");
    output.println(" ");
    for(int i =start_text;i<=end_text;i++){
      output.println("Text "+i+": "+rating.get(texto.get(str(i))));
    }
    output.println(" ");
    output.println("Textpaare");
    output.println(" ");
    int annotation = 0;
    for(int i=0;i<anno.size();i= i+2){
      
      output.print(anno.get(i)+", "+anno.get(i+1)+"\t"+"\t"+annoergebniss.get(annotation));
      output.println(" ");
      annotation = annotation +1;
  }
    println(anno.size());
    println(annoergebniss);
    output.close();
    status = "EndeAnno";
  
  }
    if ((key == 'e') && (status == "EndeAnno")){
    exit();
    
  }
  if ((key == 'o') && (status == "EndeAnno")){
    selectInput("Ergebnissdatei ausw\u00e4hlen :", "fileSelected2");
    status = "Kappa";
    
  }
    if ((key == 'k') && (status == "Kappa")){
      boolean gleicheTexte = true;
      int laufvar = 0;
      println("anno2texte"+anno2texte);
      println(annoergebniss);
      println(annoergebnisse2);
      for(int i =start_text; i<=end_text;i++){
        println("laufvar"+laufvar);
        if(anno2texte.get(laufvar)!= i){
          println("######ungleich####");
          gleicheTexte = false;
          break;
        }
        laufvar = laufvar +1 ;
      }
      if((annoergebniss.size()!=annoergebnisse2.size()|gleicheTexte == false)){
        text("Die Ergebnissdatei enth\u00e4llt falsche Annotationen",500,30);
        selectInput("Ergebnissdatei ausw\u00e4hlen :", "fileSelected2");
        //status = "EndeAnno";
      }
      else{
        println(annoergebniss);
        println(annoergebnisse2);
        for(int i =0;i<annoergebniss.size();i++){
          if(annoergebniss.get(i)==annoergebnisse2.get(i)){
            gemeinsamrelevant = gemeinsamrelevant +1;
          }
          if(annoergebniss.get(i)== 1){
            a1_1 = a1_1 +1;
          }
          if(annoergebniss.get(i)== 0){
            a1_0 = a1_0 +1;
          }
          if(annoergebnisse2.get(i)== 0){
            a2_0 = a2_0 +1;
          }
          if(annoergebnisse2.get(i)== 1){
            a2_1 = a2_1 +1;
          }
        }
        println("gemeinsam ", gemeinsamrelevant);
        println("a1_1 ", a1_1);
        println("a1_0 ", a1_0);
        println("a2_1 ", a2_1);
        println("a2_0 ", a2_0);
        pa = PApplet.parseFloat(gemeinsamrelevant)/PApplet.parseFloat(annoergebniss.size());
        println("pa "+pa);
        pe = ((PApplet.parseFloat(a1_1)/PApplet.parseFloat(annoergebniss.size()))*(PApplet.parseFloat(a2_1)/PApplet.parseFloat(annoergebniss.size())))
            +((PApplet.parseFloat(a1_0)/PApplet.parseFloat(annoergebniss.size()))*(PApplet.parseFloat(a2_0)/PApplet.parseFloat(annoergebniss.size())));
            println("pe "+pe);
        kappa = (pa-pe)/(1-pe);
        println("kappa "+kappa);
        text("Der Kappa-Wert betr\u00e4gt: "+kappa,600,600);
        if(kappa<=0.80f){
            text("Kappa wert zu niedrig bitt erneut annotieren",650,650);
            text("Daf\u00fcr Space dr\u00fccken",670,670);
            status= "anleitung";
        }
      }
    }  
}
public void fileSelected2(File selection) {
  if (selection == null) {
    text("Sie haben keine Ergebnissdatei ausgew\u00e4hlt, bitte w\u00e4hlen sie eine Ergebniss.txt-Datei aus",800,800);
    selectInput("Korpusdatei ausw\u00e4hlen :", "fileSelected");
  } else {
    
    dir = selection.getAbsolutePath();
    ergebnissdatei = new File(dir);
    String[] myErgebnissText = loadStrings (ergebnissdatei);
    for (int u=0; u < myErgebnissText.length; u++) {
      println("myergebnisse"+myErgebnissText[u]);
      Matcher matcher = Pattern.compile("Text \\d+, Text \\d+\\t\\t(\\d)").matcher(myErgebnissText[u]);
      if(matcher.find()){
      println("matcher group ### "+matcher.group(1));
      annoergebnisse2.append(PApplet.parseInt(matcher.group(1)));
      }
      Matcher matcher2 = Pattern.compile("Text (\\d+): \\d+").matcher(myErgebnissText[u]);
      if(matcher2.find()){
      println("matcher group ### "+matcher2.group(1));
      anno2texte.append(PApplet.parseInt(matcher2.group(1)));
      }
    }
    text("Ergebnissdatei eingelesen, bitte k dr\u00fccken um cohens Kappa wert auszurechnen",200,200);
    //println(annoergebnisse2);
  }
}
public void fileSelected(File selection) {
  // Korpusdatei ausw\u00e4hlen und einlesen
  
  // speicher den Pfad der ausgew\u00e4hlten Datei in der Variable dir.
  if (selection == null) {
    text("Sie haben keine Korpusdatei ausgew\u00e4hlt, bitte w\u00e4hlen sie eine .txt-Datei aus",800,800);
    selectInput("Korpusdatei ausw\u00e4hlen :", "fileSelected");
  } else {
    dir = selection.getAbsolutePath();
    println(dir);
    korpusdatei = new File(dir); // korpusdatei wird neues Objekt vom Typ File

    // korpus datei wird eingelesen und in ein String Dic geschrieben
    // "ID" : "Text"
    // Der Text wird noch mit Zeilenumbr\u00fcchen versehen,
    // damit er besser dargestellt wird.
   String regex2 = "\\t\\t";
   String text = "";
   String[] myText = loadStrings (korpusdatei);
    for (int u=0; u < myText.length; u++) {
      String [] splitresult = myText[u].split(regex2);
      int textid = PApplet.parseInt(splitresult[0]);
      String [] wordlist = splitresult[1].split(" ");
      for(int k=0;k<wordlist.length;k++){
        text += wordlist[k]+" ";
        if(k!=0&&k%10==0){
          text += "\n";
        }
      }
      korpus.set(str(textid),text);
      text = "";
  //  println(korpus);

    }
    //println(korpus.get("0"));
    //println(korpus.get("1"));
    //println(korpus.size());
    text("Korpusdatei erfolgreich eingelesen",550,600);
    text("dr\u00fccken Sie 'SPACE' um zur Anleitung zu gelangen",500,620);
    //println(korpus.get("0"));
  }
}
 // automatically receives results from controller input
public void input(String theText) {
  // Mit regul\u00e4rem ausdruck die beiden Zahlen filtern aus dem Eingabefeld
  StringList inputs = new StringList();
  Matcher matcher = Pattern.compile("\\d+").matcher(theText);
  while (matcher.find()) {
    inputs.append(matcher.group());
  }
  start_text = PApplet.parseInt(inputs.get(0));
  end_text = PApplet.parseInt(inputs.get(1));
  //println("start: "+ start_text);
  //println("end: "+ end_text);
  fill(255);
  rect(60,590,280,70);
  fill(0);
  text("Sie Annotieren die Texte: "+start_text+" - "+end_text,65,610);
  int anzahltextanno = 0;
  for(int i = start_text; i <= end_text;i++){
    anzahltextanno = anzahltextanno + 1;
  }
  
  int anzahlpaare = 0;
  int anzahlpaare2 = 0;
  for(int i=1;i<=anzahltextanno-1;i++){
    anzahlpaare = anzahlpaare +1;
    anzahlpaare2 = anzahlpaare2+anzahlpaare;
  }
  text("Anzahl der Textpaare: "+anzahlpaare2,65,640);
}
//void controlEvent(ControlEvent theEvent) {
//  if(theEvent.isFrom(r)) {
//    print("got an event from "+theEvent.getName()+"\t");
//    for(int i=0;i<theEvent.getGroup().getArrayValue().length;i++) {
//      print(int(theEvent.getGroup().getArrayValue()[i]));
//    }
//    if (theEvent.getValue() == 1.0){
//      regular = true;
//      counts = false;
//    }
//    if (theEvent.getValue() == 2.0){
//      counts = true;
//      regular = false;
//    }
//    if (theEvent.getValue() == -1.0){
//      counts = false;
//      regular = false;
//      text("bitte w\u00e4hlen Sie eine Option",800,800);
//    }    
//    println("\t "+ "regulaer :" +regular + " bestimme Zahl :" + counts);
//  }
//}oo
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "anno_2_0" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
