<!-- 기본 alert -->
<template>
  <div name="modal-alert" >
    <div class="vue-modal-box">
      <div class="dialog-content">
        <div class="dialog-c-title">
          상품명 : {{ item.product_name }} [해시태그]
        </div>
        <p class="close closeBtn" @click="$emit('close')"><i class="fas fa-times"></i></p>
        <div class="dialog-c-text" style="text-align: left;">
          <template v-if="list.length > 0">
          <template v-for="(item, idx) in list">
            <span class="inputWrap checkboxCenter" :key="idx">
              <input type="checkbox" :id="'servicetag-01-' + idx " :value="item.product_tag_text" v-model="item.selected">
              <label :for="'servicetag-01-' + idx ">{{item.product_tag_text}}</label>
            </span>
          </template>
          <div>
            <br>
            <button type="button" class="btn_min" style="width: 150px;" @click="copyClipboard()">클립보드 복사하기</button>
          </div>
          </template>
        </div>

      </div>
      <div class="dialog-button-wrapper">
        <button type="button" class="dialog-button" @click="$emit('close')">닫기</button>
      </div>
      <div>
         <input type="hidden" id="copyselect" value="">
      </div>
    </div>
    <Spinner v-if="isLoading" />
  </div>
</template>

<script>

import ModalAlert from '@/components/common/modal/ModalAlert'
export default {
  name: 'ModalHashTag',
  data () {
    return {
      isLoading: false,
      userNo: this.$store.getters.getUser.conn_no,
      tagall: this.item.product_tag_text,
      list: []
    }
  },
  props: {
    'item': Object
  },
  created () {
    this.getTagList()
  },
  methods: {
    getTagList () {
      let tagList = this.tagall.split(',')
      for (let idx in tagList) {
        let item = {}
        item.product_tag_text = tagList[idx]
        if (idx > 9) {
          item.selected = false
        } else {
          item.selected = true
        }

        this.list.push(item)
      }
    },
    copyClipboard () {
      let copyData = ''
      let isFirst = true
      for (let idx in this.list) {
        if (this.list[idx].selected) {
          if (isFirst) {
            copyData += this.list[idx].product_tag_text
            isFirst = false
          } else if (!isFirst) {
            copyData += ',' + this.list[idx].product_tag_text
          }
        }
      }

      if (copyData === '') {
        this.$modal.show(ModalAlert,
          {title: '', text: '클립보드에 복사할 태그명을 선택해주세요.'},
          { width: 450, height: 'auto' }
        )
        return
      }

      var tempElem = document.querySelector('#copyselect')
      tempElem.setAttribute('type', 'text')
      tempElem.value = copyData
      // document.body.appendChild(tempElem)

      tempElem.select()
      document.execCommand('copy')
      // document.body.removeChild(tempElem)
      this.$modal.show(ModalAlert,
        {title: '', text: '복사했습니다'},
        { width: 450, height: 'auto' }
      )

      var delElem = document.querySelector('#copyselect')
      delElem.value = ''
      delElem.setAttribute('type', 'hidden')
    }
  }
}
</script>
