<script>
import request from '@/utils/request'

const questions = []

export default {
    data () {
        return {
            questions: questions,
            question: ''
        }
    },
    methods: {
        login (parameter) {
            return request({
                url: '/ask',
                method: 'post',
                data: parameter
            })
        },
        sendQuestion () {
            this.login({ question: this.question }).then((result) => {
                this.questions.push({
                    question: this.question,
                    reply: result.message
                })
                this.question = ''
            })
        }
    }
}
</script>

<template>
  <div style="height: 100%; width: 100%; padding: 10px; overflow-y: auto;">
    <div style="display:flex; flex-direction: column; height: 100%;">
      <div style="flex-grow: 1; overflow-y: auto;">
        <div :key="index" v-for="(q, index) in questions">
          <!-- Question -->
          <a-card class="shadow">
            <div>
              <div style="display: flex;">
                <span class="ant-avatar">
                  <img src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png">
                </span>
                <div class="mx-3 my-auto">Me</div>
              </div>
              <div class="my-3 message">{{ q.question }}</div>
            </div>
          </a-card>

          <!-- Reply -->
          <a-card class="shadow my-3">
            <div>
              <div style="display: flex;">
                <span class="ant-avatar">
                  <img src="https://gw.alipayobjects.com/zos/rmsportal/mqaQswcyDLcXyDKnZfES.png">
                </span>
                <div class="mx-3 my-auto">AI</div>
              </div>
              <div class="my-3 message">{{ q.reply }}</div>
            </div>
          </a-card>

        </div>

      </div>
      <div style="display: flex">
        <a-textarea placeholder="Questions" :rows="1" v-model="question"></a-textarea>
        <a-button type="primary" @click="sendQuestion">
          发送
        </a-button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.my-3 {
    margin-top: 12px;
    margin-bottom: 12px;
}

.my-auto {
    margin-top: auto;
    margin-bottom: auto;
}

.mx-3 {
    margin-left: 12px;
    margin-right: 12px;
}

.shadow {
    box-shadow: 0 .5rem 1rem rgba(0,0,0,.15)!important;
}

.message {
    margin-left: 44px;
}

.ant-avatar {
    box-sizing: border-box;
    margin: 0;
    padding: 0;
    color: #000000d9;
    font-size: 14px;
    font-variant: tabular-nums;
    line-height: 1.5715;
    list-style: none;
    font-feature-settings: tnum;
    position: relative;
    display: inline-block;
    overflow: hidden;
    color: #fff;
    white-space: nowrap;
    text-align: center;
    vertical-align: middle;
    background: #ccc;
    width: 32px;
    height: 32px;
    line-height: 32px;
    border-radius: 50%;
}

</style>
