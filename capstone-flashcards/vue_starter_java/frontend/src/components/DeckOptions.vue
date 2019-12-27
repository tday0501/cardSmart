<template>
  <div>
    <v-card class="mx-auto" max-width="400" v-show="!alert">
      <v-list>
        <v-list-item link>
          <v-list-item-icon>
            <v-icon>mdi-playlist-edit</v-icon>
          </v-list-item-icon>
          <v-list-item-title class="subtitle-2" @click="viewCards">Edit Cards</v-list-item-title>
        </v-list-item>
        <v-list-item link>
          <v-list-item-icon>
            <v-icon>mdi-playlist-edit</v-icon>
          </v-list-item-icon>
          <v-text-field
            class="text-field subtitle-2"
            v-model="updatedName"
            label="Update Deck Name"
            clearable
            @keyup.enter="updateName()"
          ></v-text-field>
        </v-list-item>

        <v-list-item link>
          <v-list-item-icon>
            <v-icon>mdi-delete-empty</v-icon>
          </v-list-item-icon>
          <v-list-item-title class="subtitle-2" @click="alertUser">Delete</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-card>
    <div v-show="alert">
      <v-alert prominent type="error">
        <v-row align="center">
          <v-col class="grow">Are you sure? This will delete all cards in this deck</v-col>
        </v-row>
        <v-row>
          <v-btn @click="deleteDeck">Delete</v-btn>
        </v-row>
      </v-alert>
    </div>
  </div>
</template>
<script>
import auth from "@/auth.js";


export default {
  props: {
    passedDeckId: Number,
    passedSubjectId: Number,
    passedIsPublic: Boolean
  },
  data() {
    return {
      updatedName: "",
      recievedDeckId: this.passedDeckId,
      recievedSubjectId: this.passedSubjectId,
      recievedIsPublic: this.passedIsPublic,
      alert: false,
      editCard: false
    };
  },
  methods: {
    updateName: function() {
      console.log("clicked");
      fetch(process.env.VUE_APP_REMOTE_API + "/api/editDeck", {
        headers: {
          Authorization: "Bearer " + auth.getToken(),
          "Content-Type": "application/json"
        },
        method: "POST",
        body: JSON.stringify({
          deckId: this.recievedDeckId,
          subjectId: this.recievedSubjectId,
          deckName: this.updatedName,
          isPublic: this.recievedIsPublic
        })
      })
        .then(() => this.$router.push("/"))
        .then(() => this.$emit("updateDeckName"));
    },
    alertUser() {
      this.alert = !this.alert;
    },
    deleteDeck: function() {
      console.log("clicked");
      fetch(process.env.VUE_APP_REMOTE_API + "/api/deleteDeck", {
        headers: {
          Authorization: "Bearer " + auth.getToken(),
          "Content-Type": "application/json"
        },
        method: "POST",
        body: JSON.stringify({
          deckId: this.recievedDeckId,
          subjectId: this.recievedSubjectId,
          isPublic: this.recievedIsPublic
        })
      })
        .then(() => this.$router.push("/"))
        .then(() => this.$emit("deleteDeck"));
    },
    viewCards() {
      this.editCard = true;
      this.$emit("viewCards");
    }
  }
};
</script>

<style>
</style>