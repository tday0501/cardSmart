<template>
  <v-container>
    <v-toolbar dark color="grey">
      <v-btn icon dark @click="$emit('close-window')">
        <v-icon>mdi-close</v-icon>
      </v-btn>
      <v-toolbar-title>{{this.deckName}}</v-toolbar-title>
      <v-spacer></v-spacer>
    </v-toolbar>
    <v-carousel color="white" >
      <v-carousel-item v-for="card in cards" :key="card.cardId">
        <user-cards :cardId="card.cardId" :question="card.question" :answer="card.answer"/>
      </v-carousel-item>
    </v-carousel>
  </v-container>
</template>

<script>

import UserCards from "@/components/UserCards.vue";

export default {
  props: {
    deckId: Number,
    deckName: String,
    cards: Array
  },
  components: {
    UserCards
  },
  data() {
    return {
      passedDeckName: this.deckName,
      selectedDeckId: this.deckId,
      displayCards: false,
  
    };
  },
  
};
</script>

<style>
</style>