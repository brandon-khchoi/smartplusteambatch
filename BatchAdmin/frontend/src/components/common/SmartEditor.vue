<template>
  <textarea name="ir1" id="ir1" class="smartText" :rows="rowSize" cols="100" v-model="contents">
    내용을 입력해주세요.
  </textarea>
</template>

<script>
/**
 * smarteditor2-2.10.0
 * 네이버 스마트에디터
 * https://github.com/naver/smarteditor2/releases/tag/v2.10.0
 *
 * 안되거나 불편사항은 Leo 에게
 */
// eslint-disable-next-line no-unused-vars
import '@/../static/smart-editor/js/service/HuskyEZCreator.js'
export default {
  name: 'SmartEditor',
  mounted () {
    // eslint-disable-next-line no-undef
    this.sEditor = nhn.husky.EZCreator.createInIFrame({
      oAppRef: this.oEditors,
      elPlaceHolder: 'ir1',
      sSkinURI: '/static/smart-editor/SmartEditor2Skin.html',
      fCreator: 'createSEditor2',
      htParams: {
        // 툴바
        bUseToolbar: true,
        // 사이즈조절바
        bUseVerticalResizer: false,
        // 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
        bUseModeChanger: true,
        fOnBeforeUnload: function () {}
      }
    })
  },
  data () {
    return {
      sEditor: '',
      oEditors: [],
      contents: this.editorContents
    }
  },
  watch: {
  },
  methods: {
    submitContents () {
      // 에디터의 내용이 textarea에 적용된다.
      this.oEditors.getById['ir1'].exec('UPDATE_CONTENTS_FIELD', [])

      // 에디터의 내용에 대한 값 검증은 이곳에서
      // document.getElementById("ir1").value를 이용해서 처리한다.

      // textarea에 적용된 내용을 부모에 넘긴다.
      this.$parent.editorContents = this.oEditors.getById['ir1'].getIR()
    },
    pasteHtml (html) {
      this.oEditors.getById['ir1'].exec('PASTE_HTML', [html])
    }
  },
  props: {
    'editorContents': String,
    'rowSize': {
      type: Number,
      default: 20
    }
  }
}
</script>
