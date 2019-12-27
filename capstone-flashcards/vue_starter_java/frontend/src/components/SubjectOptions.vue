<template>
<div>
  <v-card class="mx-auto" max-width="400" v-show="!alert">
    <v-list>
      <v-list-item link>
        <v-list-item-icon>
          <v-icon>mdi-folder-plus</v-icon>
        </v-list-item-icon>
        <v-text-field 
               class="text-field subtitle-2"
              v-model="deckName"
              label="Add New Deck"
              clearable
              @keyup.enter ="newDeck()">
          </v-text-field>
      </v-list-item>

      <v-list-item link>
        <v-list-item-icon>
          <v-icon>mdi-playlist-edit</v-icon>
        </v-list-item-icon>
        <v-text-field 
               class="text-field subtitle-2"
              v-model="updatedName"
              label="Update Subject Name"
              clearable
              @keyup.enter ="updateName()">
          </v-text-field>
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
    <v-alert
      prominent
      type="error"
    >
      <v-row align="center">
        <v-col class="grow">Are you sure? this will delete all decks and flashcards in this subject</v-col>
       
      </v-row>
        <v-row> <v-btn @click="deleteSubject">Delete </v-btn></v-row>
    </v-alert>
    
   
  </div>
  </div>
</template>
<script>
import auth from "@/auth.js";

export default {
  props: {
    subjectsId: String,
    userId: String,
  },
  data() {
    return {
      alert: false,
      deckName: '',
      updatedName: '',
      subjectId: this.subjectsId,
      userId: this.userId,
      isPublic: 'true'
    };
  },
  methods: {
    updateName: function() {
      fetch(process.env.VUE_APP_REMOTE_API + "/api/editSubject", {
        headers: {
          Authorization: "Bearer " + auth.getToken(),
          "Content-Type": "application/json"
        },
        method: "POST",
        body: JSON.stringify({
          subjectId: this.subjectId,
          subjectName: this.updatedName,
          
        })
      }).then(() => this.$router.push("/")).then(() => this.$emit('changeSubjectName'));
      
    },
    alertUser () {
      this.alert = !this.alert;
    },
    deleteSubject: function() {
        fetch(process.env.VUE_APP_REMOTE_API + "/api/deleteSubject", {
        headers: {
          Authorization: "Bearer " + auth.getToken(),
          "Content-Type": "application/json"
        },
        method: "POST",
        body: JSON.stringify({
          subjectId: this.subjectId,
          userId: this.userId
        })
      }).then(() => this.$router.push("/")).then(() => this.$emit('deleteSubjectName'));
    },

    newDeck: function() {
      fetch(process.env.VUE_APP_REMOTE_API + "/api/decks", {
        headers: {
          Authorization: "Bearer " + auth.getToken(),
          "Content-Type": "application/json"
        },
        method: "POST",
        body: JSON.stringify({
          subjectId: this.subjectId,
          deckName: this.deckName,
          isPublic: this.isPublic
        })
      }).then(() => this.$router.push("/")).then(() => this.$emit('newDeck'));
      
    },

  }
};
</script>

<style>
</style>