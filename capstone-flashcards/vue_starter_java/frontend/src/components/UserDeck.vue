<template>
  <v-card color="blue lighten-2" width="300" height="150" elevation="2" class="white--text mx-auto layout" 
   @click.stop="showCards">
    <v-card-title class="title">{{passedDeckName}}</v-card-title>
    <v-menu top right >
      <template v-slot:activator="{ on }" top right >
        <v-icon v-on="on" @click.stop="deckOptions = true" class="deckOptions">mdi-dots-horizontal</v-icon>
      </template>

      

      <v-dialog v-model="deckOptions" max-width="300px">
        <deck-options
          :passedDeckId="this.passedDeckId"
          :passedSubjectId="this.passedSubjectId"
          :passedIsPublic="this.passedIsPublic"
          v-on:updateDeckName="deckChange"
          v-on:deleteDeck="deckChange"
          v-on:viewCards="openViewCards"
        />
      </v-dialog>
    </v-menu>
    <v-menu top >
      <template v-slot:activator="{ on }">
        <v-icon v-on="on" @click.stop="cardForm = true" class="cardForm">mdi-note-plus-outline</v-icon>
      </template>
      
      <v-dialog v-model="cardForm" max-width="300px">
        <card-form :deckId="this.passedDeckId" v-on:newCard="addedNewCard"/>
      </v-dialog>
    </v-menu>
    <v-dialog v-model="displayCards" fullscreen hide-overlay transition="dialog-bottom-transition">
      <cards-display :cards="this.cards" :deckId="this.passedDeckId" :deckName="this.passedDeckName" @close-window="displayCards = false" />
    </v-dialog>
    <v-dialog v-model="editCard" fullscreen hide-overlay transition="dialog-bottom-transition">
      <all-deck-cards v-on:closeWindow="closeAllDecks" :deckId="this.passedDeckId"/>
    </v-dialog>

  </v-card>
</template>

<script>
import CardsDisplay from "@/components/CardsDisplay.vue";
import DeckOptions from "@/components/DeckOptions.vue";
import CardForm from "@/components/CardForm.vue";
import AllDeckCards from "@/components/AllDeckCards.vue";
import auth from "@/auth.js";


export default {
  props: {
    deckName: String,
    deckId: Number,
    subjectId: Number,
    isPublic: Boolean
  },

  data() {
    return {
      editCard: false,
      addCardDialog: false,
      passedSubjectId: this.subjectId,
      passedIsPublic: this.isPublic,
      passedDeckName: this.deckName,
      passedDeckId: this.deckId,
      displayCards: false,
      dialog: false,
      deckOptions: false,
      cardForm: false,
      cards: []
    };
  },

  components: {
    CardsDisplay,
    DeckOptions,
    CardForm,
    AllDeckCards
  },
  methods: {
    showCards() {
       fetch(
          process.env.VUE_APP_REMOTE_API +
            "/api/cards?deckId=" +
            this.passedDeckId,
          {
            headers: { Authorization: "Bearer " + auth.getToken() }
          }
        )
          .then(response => response.json())
          .then(data => {
            if (data.length > 0) {

            
            this.cards = data;
            this.displayCards = true;
            } 
          } );
    },
    deckChange() {
      this.$emit('didDeckChange', 2)
      this.deckOptions = !this.deckOptions
    }, 
    addedNewCard(){
      this.cardForm = !this.cardForm
    },
    openViewCards(){
      this.editCard = !this.editCard
    },
    closeAllDecks(){
      this.editCard = false
    }
  }
};
</script>

<style>
.layout{
  display: grid;
  grid-template-columns: 8fr 1fr 1fr .5fr;
  grid-template-areas: 
  ". deckOptions cardform ."
  "title title title title"
  ". . . ."
  ". . . ."
}

.title {
  grid-area: title;
  display: flex;
  justify-content: center;
}

.deckOptions {
  grid-area: deckOptions;
}

.cardForm {
  grid-area: cardform
}

</style>
