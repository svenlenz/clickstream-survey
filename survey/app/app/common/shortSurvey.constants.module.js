angular.module('constants',[]).
    constant('shortSurvey',  {
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
    },
    {
     type: "radiogroup",
     choices: [
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
     isRequired: true,
     name: "technique",
     title: "Ich bin erfahren im Umgang mit elektronischen Geräten (Computer, Mobilephone, ...)"
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
