angular.module('constants').
    constant('fullSurvey',  {
 completedHtml: "<p><h4>Besten Dank für das Ausfüllen des ersten Teil.</h4></p><p>Die nächste Aufgabe ist das Klicken durch den Prototyp: <button type='button' class='btn btn-info btn-block' ng-click='submit()' >Zum Prototypen</button></p>",
 pageNextText: "Weiter",
 pagePrevText: "Zurück",
   pages: [
  {
   questions: [
    {
     type: "radiogroup",
     choices: [
      {
       value: "1",
       text: "Das ist mein erstes Mal"
      },
      {
       value: "2",
       text: "Das ist mein zweites Mal"
      },
      {
       value: "3",
       text: "Mehr als zwei Mal"
      }
     ],
     isRequired: true,
     name: "runs",
     title: "Wie oft haben Sie schon an diesem Test teilgenommen?"
    },
    {
     type: "radiogroup",
     choices: [
      {
       value: "W",
       text: "weiblich"
      },
      {
       value: "M",
       text: "männlich"
      }
     ],
     isRequired: true,
     name: "gender",
     title: "Ihr Geschlecht:"
    },
    {
     type: "radiogroup",
     choices: [
      {
       value: "1",
       text: "jünger als 20 Jahre"
      },
      {
       value: "2",
       text: "20 bis 30 Jahre"
      },
      {
       value: "3",
       text: "31 bis 40 Jahre"
      },
      {
       value: "4",
       text: "41 bis 50 Jahre"
      },
      {
       value: "5",
       text: "älter als 50 Jahre"
      }
     ],
     isRequired: true,
     name: "age",
     title: "Wie alt sind Sie?"
    }
   ],
   name: "Stats",
   title: "Für statistische Zwecke"
  },
  {
   questions: [
    {
     type: "matrix",
     columns: [
      {
       value: "0",
       text: "trifft gar nicht zu"
      },
      {
       value: "1",
       text: "trifft eher nicht zu"
      },
      {
       value: "2",
       text: "trifft eher zu"
      },
      {
       value: "3",
       text: "trifft genau zu"
      }
     ],
     isAllRowRequired: true,
     isRequired: true,
     name: "big5_answers",
     rows: [
      {
       value: "neuro1",
       text: "Ich bin ein ängstlicher Typ."
      },
      {
       value: "ehrlich2",
       text: "Im privaten Bereich habe ich schon mal Dinge gemacht, die besser nicht an die Öffentlichkeit kommen sollten."
      },
      {
       value: "leistung4",
       text: "Am glücklichsten bin ich dann, wenn viele Menschen mich bewundern und das toll finden, was ich mache."
      },
      {
       value: "neuro5",
       text: "Ich grübele viel über meine Zukunft nach."
      },
      {
       value: "neuro6",
       text: "Oft überwältigen mich meine Gefühle."
      },
      {
       value: "neuro10",
       text: "Ich bin mir in meinen Entscheidungen oft unsicher."
      },
      {
       value: "extra1",
       text: "Ich bin gerne mit anderen Menschen zusammen."
      },
      {
       value: "neuro9",
       text: "Oft werde ich von meinen Gefühlen hin- und her gerissen."
      },
      {
       value: "extra6",
       text: "Ich bin ein Einzelgänger. "
      },
      {
       value: "offen1",
       text: "Ich will immer neue Dinge ausprobieren."
      },
      {
       value: "extra8",
       text: "Ich bin in vielen Vereinen aktiv."
      },
      {
       value: "extra9",
       text: "Ich bin ein gesprächiger und kommunikativer Mensch."
      },
      {
       value: "macht6",
       text: "Ich kann Menschen verstehen, die sagen, dass andere Dinge wichtiger sind als Einfluss und Politik. "
      },
      {
       value: "sicher1",
       text: "Ich habe schon immer ein starkes Bedürfnis nach Sicherheit und Ruhe verspürt."
      },
      {
       value: "gewissen6",
       text: "Auch kleine Bußgelder sind mir sehr unangenehm."
      },
      {
       value: "neuro2",
       text: "Ich fühle mich oft unsicher."
      },
      {
       value: "neuro3",
       text: "Ich verspüre oft eine große innere Unruhe."
      },
      {
       value: "extra5",
       text: "Im Grunde bin ich oft lieber für mich allein."
      },
      {
       value: "gewissen1",
       text: "Ich bin sehr pflichtbewusst."
      },
      {
       value: "vertrag2",
       text: "Ich bin ein höflicher Mensch."
      },
      {
       value: "gewissen2",
       text: "Meine Aufgaben erledige ich immer sehr genau."
      },
      {
       value: "vertrag3",
       text: "Ich helfe anderen, auch wenn man mir es nicht dankt."
      },
      {
       value: "vertrag4",
       text: "Ich habe immer wieder Streit mit anderen."
      },
      {
       value: "sicher5",
       text: "Ich träume oft von einem ruhigen Leben ohne böse Überraschungen."
      },
      {
       value: "sicher6",
       text: "Am glücklichsten bin ich dann, wenn ich mich geborgen fühle."
      },
      {
       value: "macht2",
       text: "Wenn ich die Wahl hätte, würde ich in meinem Leben gerne weltbewegende Entscheidungen treffen."
      },
      {
       value: "macht4",
       text: "Für mehr Einfluss würde ich auf vieles verzichten."
      },
      {
       value: "neuro4",
       text: "Ich mache mir oft unnütze Sorgen."
      },
      {
       value: "ehrlich3",
       text: "Ich habe schon mal Dinge weitererzählt, die ich besser für mich behalten hätte."
      },
      {
       value: "leistung3",
       text: "Für mehr Anerkennung würde ich auf vieles verzichten."
      },
      {
       value: "gewissen3",
       text: "Ich war schon als Kind sehr ordentlich."
      },
      {
       value: "gewissen4",
       text: "Ich gehe immer planvoll vor."
      },
      {
       value: "vertrag8",
       text: "Es fällt mir sehr leicht, meine Bedürfnisse für andere zurückzustellen."
      },
      {
       value: "extra10",
       text: "Ich bin sehr kontaktfreudig."
      },
      {
       value: "vertrag9",
       text: "Ich kann mich gut in andere Menschen hinein versetzen."
      },
      {
       value: "vertrag10",
       text: "Ich komme immer gut mit anderen aus, auch wenn sie nicht meiner Meinung sind."
      },
      {
       value: "leistung1",
       text: "Ich habe schon immer ein starkes Bedürfnis verspürt nach meinen eigenen Maßstäben der Beste zu sein. "
      },
      {
       value: "neuro7",
       text: "Ich bin oft ohne Grund traurig."
      },
      {
       value: "gewissen8",
       text: "Ich achte sehr darauf, dass Regeln eingehalten werden."
      },
      {
       value: "offen2",
       text: "Ich bin ein neugieriger Mensch."
      },
      {
       value: "offen5",
       text: "Ich diskutiere gerne."
      },
      {
       value: "gewissen5",
       text: "Ich habe meine festen Prinzipien und halte daran auch fest."
      },
      {
       value: "macht3",
       text: "Tief in meinem Innersten gibt es eine Sehnsucht nach Einfluss und Macht."
      },
      {
       value: "extra2",
       text: "Ich kann schnell gute Stimmung verbreiten."
      },
      {
       value: "offen3",
       text: "Ich reise viel, um andere Kulturen kennenzulernen."
      },
      {
       value: "extra7",
       text: "Ich gehe gerne auf Partys."
      },
      {
       value: "gewissen9",
       text: "Wenn ich mich einmal entschieden habe, dann weiche ich davon auch nicht mehr ab."
      },
      {
       value: "gewissen10",
       text: "Ich mache eigentlich nie Flüchtigkeitsfehler."
      },
      {
       value: "neuro8",
       text: "Ich bin oft nervös."
      },
      {
       value: "offen4",
       text: "Am liebsten ist es mir, wenn alles so bleibt, wie es ist."
      },
      {
       value: "gewissen7",
       text: "Auch kleine Schlampereien stören mich."
      },
      {
       value: "offen6",
       text: "Ich lerne immer wieder gerne neue Dinge."
      },
      {
       value: "offen7",
       text: "Ich beschäftige mich viel mit Kunst, Musik und Literatur."
      },
      {
       value: "vertrag1",
       text: "Ich achte darauf, immer freundlich zu sein."
      },
      {
       value: "ehrlich1",
       text: "Ich habe schon mal etwas unterschlagen oder nicht gleich zurückgegeben."
      },
      {
       value: "vertrag5",
       text: "Ich bin ein Egoist."
      },
      {
       value: "vertrag7",
       text: "Ich würde meine schlechte Laune nie an anderen auslassen."
      },
      {
       value: "offen8",
       text: "Ich interessiere mich sehr für philosophische Fragen."
      },
      {
       value: "offen9",
       text: "Ich lese viel über wissenschaftliche Themen, neue Entdeckungen oder historische Begebenheiten."
      },
      {
       value: "offen10",
       text: "Ich habe viele Ideen und viel Fantasie."
      },
      {
       value: "leistung2",
       text: "Ich habe schon immer ein starkes Bedürfnis nach Anerkennung und Bewunderung verspürt."
      },
      {
       value: "macht5",
       text: "Am glücklichsten bin ich dann, wenn ich Verantwortung übernehmen kann und wichtige Entscheidungen treffen darf."
      },
      {
       value: "sicher2",
       text: "Wenn ich die Wahl hätte, würde ich ein Leben in Sicherheit und Frieden wählen."
      },
      {
       value: "extra3",
       text: "Ich bin unternehmungslustig."
      },
      {
       value: "extra4",
       text: "Ich stehe gerne im Mittelpunkt."
      },
      {
       value: "leistung5",
       text: "Tief in meinem Innersten gibt es eine Sehnsucht danach der Beste sein zu wollen."
      },
      {
       value: "leistung6",
       text: "Ich träume oft davon, berühmt zu sein."
      },
      {
       value: "macht1",
       text: "Ich träume oft davon, wichtige Entscheidungen für Politiker oder andere mächtige Menschen zu treffen."
      },
      {
       value: "sicher3",
       text: "Für ein sicheres Leben ohne böse Überraschungen würde ich auf vieles verzichten."
      },
      {
       value: "vertrag6",
       text: "Wenn mir jemand hilft, erweise ich mich immer als dankbar."
      },
      {
       value: "sicher4",
       text: "Tief in meinem Innersten gibt es eine Sehnsucht nach Ruhe und Geborgenheit."
      },
      {
       value: "ehrlich4",
       text: "Ich habe schon mal über andere gelästert oder schlecht über sie gedacht."
      }
     ],
     title: "Inwieweit treffen die folgenden Aussagen auf Sie zu? Antworten Sie möglichst spontan. Es gibt keine richtigen oder falschen Antworten."
    }
   ],
   name: "big5",
   title: "Anonymer Persönlichkeitstest"
  },
  {
   questions: [
    {
     type: "comment",
     name: "about",
     title: "Feedback"
    }
   ],
   name: "Feedback"
  }
 ],
 title: ""
});
