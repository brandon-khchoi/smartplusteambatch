<template>
  <div name="modal-alert" >
    <div class="vue-modal-box">
      <div class="dialog-content">
        <div class="dialog-c-title">
          NEWS 미리보기
        </div>
        <div class="">
          <div class="newsPreviewWrap">
            <div class="NewsIndex" >
              <div class="section newsblock" style="padding: 10px 0px 0px 10px">
                <div class="sub-news">
                  <div class="inner" style="margin-right: 0px;margin-left: 25px;">
                    <div class="item-wrap">
                      <div class="item">
                        <a
                          :href="newsUrlAddr"
                          target="_blank"
                        >
                          <img :src="uploadImageFile" />
                          <div class="txt-box" style="font-family: NanumBarunGothic,'Noto Sans KR',sans-serif;">
                            <span class="cate">[{{ newsTypeCodeDesc }}]</span>
                            <p class="tit">{{ newsTitle }}</p>
                          </div>
                        </a>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="dialog-button-wrapper">
        <button type="button" class="dialog-button" @click="$emit('close')">닫기</button>
      </div>
    </div>
  </div>
</template>

<script>

import('@/styles/newinfo.css')
export default {
  data () {
    return {
    }
  },
  props: {
    'newsUrlAddr': String,
    'uploadImageFile': String,
    'newsTypeCodeDesc': String,
    'newsTitle': String
  }
}
</script>
