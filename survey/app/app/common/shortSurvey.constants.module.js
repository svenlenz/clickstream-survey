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
    }
   ],
   title: "Für statistische Zwecke"
  },
  {
   name: "Psycho",
   questions: [
    {
     type: "matrix",
     name: "Psycho",
     title: "Inwieweit treffen die folgenden Aussagen auf Sie zu? Antworten Sie möglichst spontan. Es gibt keine richtigen oder falschen Antworten.",
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
