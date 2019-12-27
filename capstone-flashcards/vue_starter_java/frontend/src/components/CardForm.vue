<template>
    <v-card class="mx-auto" max-width="400" v-show="!alert">
      <v-card-title>Add a Card</v-card-title>
      <v-list>
        <v-list-item link>
          <v-text-field
            class="text-field subtitle-2"
            v-model="question"
            label="Please enter a question"
            clearable
          ></v-text-field>
        </v-list-item>
        <v-list-item link>
          <v-textarea
            class="text-area subtitle-2"
            v-model="answer"
            label="Please enter an answer"
            clearable
            rows="4"
          ></v-textarea>
        </v-list-item>
        <v-list-item>
          <v-btn
            width="150"
            absolute
            top
            right
            small
            color="green lighten-1"
            @click="addCard"
          >Submit</v-btn>
        </v-list-item>
      </v-list>
    </v-card>
</template>

<script>
import auth from "@/auth.js";

export default {
  props: {
    deckId: Number
  },
  data() {
    return {
      question: '', 
      answer: '', 
      getDeckId: this.deckId
    }
  },
  methods: {
    addCard: function() {
      fetch(process.env.VUE_APP_REMOTE_API + "/api/cards", {
        headers: {
          Authorization: "Bearer " + auth.getToken(),
          "Content-Type": "application/json"
        },
        method: "POST",
        body: JSON.stringify({
          deckId: this.getDeckId,
          question: this.question,
          answer: this.answer
        })
      })
        .then(() => this.question='')
        .then(() => this.answer='')
        .then(() => this.$router.push("/"))
        .then(() => this.$emit("newCard"));
    }
  }
};
</script>

<style>
</style>