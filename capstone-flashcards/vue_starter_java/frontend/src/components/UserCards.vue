<template>
    <v-sheet
        :cardId="passedCardId"
        color="green lighten-3"
        height="100%"
        tile>
    <div
          class="fill-height layout-container"
        >
        <div class="content">
        <div class="display-3" v-show="isQuestion">{{this.passedQuestion}}</div>
        <div class="display-3" v-show="!isQuestion">{{this.passedAnswer}}</div>
        </div>

        <div class="button">
        <v-btn 
        @click="flipCard" v-show="isQuestion"
        >Show Answer</v-btn>
        <v-btn 
        @click="flipCard" v-show="!isQuestion"
        >Show Question</v-btn>
        </div>
    </div>
    </v-sheet>
</template>

<script>


export default {
    props:{
        question: String,
        answer: String,
        cardId: Number
    },
   
    data () {
        return {
        isQuestion: true,
         passedQuestion: this.question,
         passedAnswer: this.answer,
         passedCardId: this.cardId,
         count: 0,
         editCard: false,
        card: {
          cardId: "",
          deckId: "",
          question: "",
          answer: "",
          imagePath: ""
        },
        absolute: false,
        opacity: 1,
        overlay: false,
        }
    },
    methods : {
        flipCard () {
          this.isQuestion = !this.isQuestion
        }
    }
        
    };

</script>

<style>
  .layout-container {
    display: grid;
    grid-template-columns: 1fr 3fr 1fr;
    grid-template-areas: 
    " . . ."
    ". content ."
    ". button ."
    ". . .";

  }

  .content {
    grid-area: content;
    display: flex;
    justify-content: center;
    align-items: flex-end
  }
  .button {
    grid-area: button;
    display: flex;
    justify-content: center;
    align-items: center
  }

</style>