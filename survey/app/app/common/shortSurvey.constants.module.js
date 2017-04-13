angular.module('constants',[]).
    constant('shortSurvey',  {
 completedHtml: "<p><h4>Besten Dank für das Ausfüllen des ersten Teil.</h4></p><p>Die nächste Aufgabe ist das Klicken durch den Prototyp: <button type='button' class='btn btn-info btn-block' ng-click='submit()' >Zum Prototypen</button></p>",
 pageNextText: "Weiter",
 pagePrevText: "Zurück",
 pages: [
  {
   name: "Statistik",
   questions: [
    {
     type: "radiogroup",
     name: "Statistik",
     title: "Wie oft haben Sie schon an diesem Test teilgenommen?",
     isRequired: true,
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
     ]
    },
    {
     type: "radiogroup",
     name: "Ihr Geschlecht:",
     isRequired: true,
     choices: [
      {
       value: "W",
       text: "weiblich"
      },
      {
       value: "M",
       text: "männlich"
      }
     ]
    },
    {
     type: "radiogroup",
     name: "Wie alt sind Sie?",
     isRequired: true,
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
     ]
    },
   ],
   title: "Für statistische Zwecke"
  },
  {
   name: "Psycho",
   questions: [
    {
     type: "matrix",
     name: "Inwieweit treffen die folgenden Aussagen auf Sie zu? Antworten Sie möglichst spontan. Es gibt keine richtigen oder falschen Antworten.",
     isRequired: true,
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
     rows: [
      {
       value: "1",
       text: "Ich bin ein ängstlicher Typ."
      },
      {
       value: "2",
       text: "Im privaten Bereich habe ich schon mal Dinge gemacht, die besser nicht an die Öffentlichkeit kommen sollten."
      }
     ],
     isAllRowRequired: true
    }
   ],
   title: "Anonymer Persönlichkeitstest"
  }
 ],
 title: ""
});
