<template>
  <v-container>
    <v-toolbar dark color="blue lighten-1">
      <v-btn icon dark @click="$emit('closeWindow')">
        <v-icon>mdi-close</v-icon>
      </v-btn>
      <div v-show="isQuizClicked">
          <v-btn icon dark color="red" @click="wrong"> <!--:disabled="questionSubmitted"> -->
            <v-icon>mdi-adjust</v-icon>  
        </v-btn>
        <v-btn icon dark color="green darken-8" @click="correct"> <!-- :disabled="questionSubmitted"> -->
            <v-icon>mdi-check-outline</v-icon>
        </v-btn>
      </div>
      <div class="my-2" v-show="isQuizClicked">

        <template>
  <div class="text-center">
    <v-dialog
      v-model="dialog"
      width="500"
    >
      <template v-slot:activator="{ on }">
        <v-btn
          color="primary darken-8"
          dark
          v-on="on"
        >
          Complete
        </v-btn>
      </template>

      <v-card>
        <v-card-title
          class="headline grey lighten-2"
          primary-title
        >
          Quiz Score
        </v-card-title>

        <v-card-text>
          <p>{{this.correctQuestions.length}} Questions Correct out of {{numQuestions()}}</p>
        </v-card-text>

        <v-divider></v-divider>

        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            color="primary"
            text
            @click="dialog = false"
          >
            Close Window
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>
      </div>
      <v-toolbar-title v-show="selectDecks">Please Select Deck</v-toolbar-title>
      <v-spacer></v-spacer>
    </v-toolbar>
     <v-carousel color="white" v-show="isQuizClicked" max-height="200" :continuous="false">
       
       <v-carousel-item v-for="card in cards" :key="card.cardId">
              <user-cards :cardId="card.cardId" :question="card.question" :answer="card.answer"/>
       </v-carousel-item>
     </v-carousel>
     <v-container class="cardContainer" v-show="selectDecks">
       <v-card
         :color="isClicked ? 'green' : 'blue lighten-2'"
         width="300"
         height="150"
         elevation="2"
         class="white--text mx-auto layout card-format"
         v-for="deck in decks"
         :key="deck.deckName"
         @click="addQuizDecks(deck.deckId)"
       >
         <v-card-title class="title">{{deck.deckName}}</v-card-title>
       </v-card>
     </v-container>
    <v-toolbar dark color="blue lighten-1" v-show="selectDecks">
      <v-btn @click="takeQuiz()" color="error" v-show="showTakeQuiz">Take Quiz</v-btn>
      <v-spacer></v-spacer>
    </v-toolbar>
  </v-container>
</template>

<script>
import auth from "@/auth.js";
import UserCards from "@/components/UserCards.vue";

export default {
  data() {
    return {

      cardId: 1,
      dialog: false,
      showTakeQuiz: false,
      selectDecks: true,
      isQuizClicked: false,
      active:false,
      decks: [],
      quizDecks: [],
      isClicked: false,
      cards: [],
      correctQuestions: [],
      wrongQuestions: [],
      questionSubmitted: false
    };
  },
   components: {
    UserCards
  },
  methods: {
      
       
      addQuizDecks (deckId) {
          if (this.quizDecks.includes(deckId)) {
              const result = this.quizDecks.filter(id => id != deckId);
              this.active = !this.active;
              this.quizDecks = result;
              this.isClicked = false;
              this.showQuizButton();
          } else {
              this.quizDecks.unshift(deckId);
              this.isClicked = true;
              this.showQuizButton();
          }
          
      },
      takeQuiz () {
          fetch(
          process.env.VUE_APP_REMOTE_API +
            "/api/quizUserCards?quizDecks=" +
            this.quizDecks,
          {
            headers: { Authorization: "Bearer " + auth.getToken() }
          }
        )
          .then(response => response.json())
          .then(data => {
            if (data.length > 0) {
              this.cards = data;
              this.isQuizClicked = true
              this.selectDecks = false
            } else {
              
            }
          })
      },
      showQuizButton() {
         
          if (this.quizDecks.length > 0) {
              this.showTakeQuiz = true;
          } else {
              this.showTakeQuiz = false;
          }
      },
      correct() {
          this.correctQuestions.unshift(this.cardId);
          this.questionSubmitted = true;
      },
      wrong() {
          this.wrongQuestions.unshift(this.cardId);
          this.questionSubmitted = true;
      },
      numQuestions() {
        return (this.correctQuestions.length + this.wrongQuestions.length);
      }
  },
  created() {
    fetch(process.env.VUE_APP_REMOTE_API + "/api/allUserDecks", {
      headers: { Authorization: "Bearer " + auth.getToken() }
    })
      .then(response => response.json())
      .then(data => (this.decks = data));
  }
};
</script>

<style>
.cardContainer {
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  height: 70vh;
  background-color: white;
  overflow-y: scroll;
}

.card-format {
  max-width: 15em;
  max-height: 10em;
  margin: 1em 0;
}
</style>