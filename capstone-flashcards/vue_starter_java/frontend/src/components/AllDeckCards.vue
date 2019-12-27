<template>
  <v-container>
    <v-toolbar dark color="blue lighten-1">
      <v-btn icon dark @click="$emit('closeWindow')">
        <v-icon>mdi-close</v-icon>
      </v-btn>
      <v-toolbar-title>Please Select Card to Edit</v-toolbar-title>
      <v-spacer></v-spacer>
    </v-toolbar>
    
    <v-container class="cardContainer">
      <v-card
        color="blue lighten-2"
              
        class="white--text mx-auto layout card-format"        
        v-for="card in cards"
        :key="card.cardId"
        @click.stop="handleEditCard(card.cardId, card.question, card.answer, card.imagePath)"
      >
        <v-row>
          
        <v-card-title class="title"> {{card.question}}</v-card-title>
          
        </v-row>
        <v-row>
          
        <v-card-title class="title">{{card.answer}}</v-card-title>
         
        </v-row>
        
        
        
      </v-card>
      <v-dialog v-model="editCard">
        <card-options :cardId="currentClickedCardId" 
        :question="currentClickedCardQuestion" 
        :answer="currentClickedCardAnswer" 
        :imagePath="currentClickedCardImagePath" 
        v-on:editCard="true"
        v-on:cardChanged="editCard=false"
        v-on:cardDeleted="editCard=false"/>
        </v-dialog>
    </v-container>
    <v-toolbar dark color="blue lighten-1">
      <v-btn @click="$emit('closeWindow')" color="error">Close</v-btn>

      <v-spacer></v-spacer>
    </v-toolbar>
  </v-container>
</template>

<script>
import auth from "@/auth.js";
import CardOptions from "@/components/CardOptions.vue";

export default {
  props: {
    deckId: Number
  },
  components: {
    CardOptions
  },
  data() {
    return {
      recievedDeckId: this.deckId,
      editCard: false,
      cards: [
      ],
      currentClickedCardId: '',
      currentClickedCardQuestion: '',
      currentClickedCardAnswer: '',
      currentClickedCardImagePath: ''
      
    };
  },
  methods: {
    handleEditCard(cardId, question, answer, imagePath) {
      this.$emit("passedCardId", this.cardId);
      this.editCard=true;
      this.currentClickedCardId=cardId;
      this.currentClickedCardQuestion=question;
      this.currentClickedCardAnswer=answer;
      this.currentClickedCardImagePath=imagePath;
      
    }
  },
  created() {
    fetch(process.env.VUE_APP_REMOTE_API + "/api/cards?deckId=" + this.recievedDeckId, {
      headers: { Authorization: "Bearer " + auth.getToken() }
    })
      .then(response => response.json())
      .then(data => (this.cards = data));
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
.title {
  font-size: .5em;
}
</style>