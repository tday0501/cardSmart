<template>
<div>
<v-card class="mx-auto" max-width="400">
    <v-form>   
        <v-container>
            <v-text-field
            v-model="question"
            label="Question"
            clearable
            required></v-text-field>
            <v-text-field
            v-model="answer"
            label="Answer"
            clearable
            required></v-text-field>
            <v-text-field
            v-model="imagePath"
            label="Image Url"
            clearable
            required></v-text-field>           
            <v-btn @click="addNewDeck" width="150" absolute bottom right small color="green lighten-1">Submit</v-btn>
        </v-container>
    </v-form>
</v-card>
</div>
</template>

<script>
import auth from '@/auth.js'

export default {
    data() {
        return {    
            deckId: '',
           question: '',
           answer: '',
           imagePath: ''
        }
    }, 
    methods: {
        addNewDeck () {
      fetch(process.env.VUE_APP_REMOTE_API +  '/api/cards', {
                   headers: {Authorization: 'Bearer ' + auth.getToken(), 'Content-Type': 'application/json'},
        method: 'POST',
        body: JSON.stringify({deckId: this.deckId, question: this.question, answer: this.answer, imagePath: this.imagePath})
      })
      .then(() => this.$router.push('/'));
      },
    }

}
</script>

<style>

</style>