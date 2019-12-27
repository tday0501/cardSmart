<template>
<div>
  <v-card class="mx-auto" max-width="400" v-show="!alert">
    <v-list >
      

      <v-list-item link>
        <v-list-item-icon>
          <v-icon>mdi-playlist-edit</v-icon>
        </v-list-item-icon>
        <v-text-field 
               class="text-field subtitle-2"
              v-model="question"
              label="Update Question"
              clearable 
               >
               
          </v-text-field>
      </v-list-item>
      <v-list-item>
        <v-list-item-icon>
          <v-icon>mdi-playlist-edit</v-icon>
        </v-list-item-icon>
          <v-text-field 
               
              v-model="answer"
              label="Update Answer"
              
               >
          </v-text-field>
      </v-list-item>
      <v-list-item>
        <v-list-item-icon>
          <v-icon>mdi-playlist-edit</v-icon>
        </v-list-item-icon>
          <v-text-field 
               class="text-field subtitle-2"
              v-model="imagePath"
              label="Update Image"
              clearable
             >
          </v-text-field>
      </v-list-item>

<v-btn @click="updateCard"> Update Card</v-btn>
<v-btn @click="alertUser"> Delete Card</v-btn>
    </v-list>
 
  </v-card>
   <div v-show="alert">
    <v-alert
      prominent
      type="error"
    >
      <v-row align="center">
        <v-col class="grow">Are you sure you want to delete this card?</v-col>
       
      </v-row>
        <v-row> <v-btn :cardId="cardId" @click="deleteCard">Delete </v-btn> </v-row>
    </v-alert>
    
   
  </div>
  </div>
</template>
<script>
import auth from "@/auth.js";

export default {
  props: {
    cardId: Number,
    question: String,
    answer: String,
    imagePath: String
    
  },
  data() {
    return {
      alert: false,
      
          
    };
  },
  
     
  
  methods: {
    updateCard: function() {
      console.log('clicked');
     
      
      fetch(process.env.VUE_APP_REMOTE_API +  '/api/updateCard', {
                   headers: {Authorization: 'Bearer ' + auth.getToken(), 'Content-Type': 'application/json'},
        method: 'POST',
        body: JSON.stringify({cardId: this.cardId, 
        question: this.question,
        answer: this.answer, 
        imagePath: this.imagePath})
      })
      .then(() => this.$router.push('/')).then(() => this.$emit('cardChanged'));
      
    },
    alertUser () {
      this.alert = !this.alert;
    },
    deleteCard: function() {
        fetch(process.env.VUE_APP_REMOTE_API + "/api/deleteCard", {
        headers: {
          Authorization: "Bearer " + auth.getToken(),
          "Content-Type": "application/json"
        },
         method: "POST",
        body: JSON.stringify({
            cardId: this.cardId
        })
        
      }).then(() => this.$router.push("/")).then(() => this.$emit('cardDeleted'));
    },
    },
};
</script>

<style>
</style>