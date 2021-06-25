<template>
  <div>
    <h4 class="lighter">
      <router-link to="/business/course" class="ace-icon fa fa-reply icon-only pink2">
        {{ course.name }}
      </router-link>
      ：
      <router-link to="/business/chapter" class="ace-icon fa fa-reply icon-only pink2">
        {{ chapter.name }}
      </router-link>
    </h4>
    <hr>
    <p>
      <button v-on:click="add()" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-edit"></i>
        新增
      </button>
      &nbsp;
      <button v-on:click="list(1)" class="btn btn-white btn-default btn-round">
        <i class="ace-icon fa fa-refresh"></i>
        刷新
      </button>
    </p>

    <pagination ref="pagination" v-bind:list="list"></pagination>

    <table id="simple-table" class="table table-bordered table-hover">
      <thead>
      <tr>
        <th>id</th>
        <th>标题</th>
        <th>视频</th>
        <th>时长</th>
        <th>收费</th>
        <th>顺序</th>
        <th>操作</th>
      </tr>
      </thead>

      <tbody>
      <tr v-for="section in sections">
        <td>{{ section.id }}</td>
        <td>{{ section.title }}</td>
        <td>{{ section.video }}</td>
        <td>{{ section.time }}</td>
        <td>{{ SECTION_CHARGE | optionKV(section.charge) }}</td>
        <td>{{ section.sort }}</td>
        <td>
          <div class="btn-group">
            <button v-on:click="edit(section)" class="btn btn-white btn-xs btn-info btn-round">
              编辑
            </button>&nbsp;
            <button v-on:click="del(section.id)" class="btn btn-white btn-xs btn-warning btn-round">
              删除
            </button>
          </div>
        </td>
      </tr>
      </tbody>
    </table>

    <div id="form-modal" class="modal fade" tabindex="-1" role="dialog">
      <div class="modal-dialog modal-dialog-scrollable">
        <div class="modal-content">
          <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                aria-hidden="true">&times;</span></button>
            <h4 class="modal-title">表单</h4>
          </div>
          <div class="modal-body">
            <form class="form-horizontal">
              <div class="form-group">
                <label for="title" class="col-sm-2 control-label">标题</label>
                <div class="col-sm-10">
                  <input v-model="section.title" type="text" class="form-control" id="title" placeholder="标题">
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">课程</label>
                <div class="col-sm-10">
                  <p class="form-control-static">{{ course.name }}</p>
                </div>
              </div>
              <div class="form-group">
                <label class="col-sm-2 control-label">大章</label>
                <div class="col-sm-10">
                  <p class="form-control-static">{{ chapter.name }}</p>
                </div>
              </div>
              <div class="form-group">
                <label for="video" class="col-sm-2 control-label">视频</label>
                <div class="col-sm-10">
                  <file
                      v-bind:suffixs="['mp4']"
                      v-bind:use="FILE_USE.COURSE.key"
                      v-bind:input-id="'video-upload'"
                      v-bind:text="'上传视频'"
                      v-bind:after-upload="afterUpload"></file>
                  <div v-show="section.video" class="row">
                    <div class="col-md-9">
                      <video v-bind:src="section.video" id="video" controls="controls"></video>
                    </div>
                  </div>
                </div>
              </div>
              <div class="form-group">
                <label for="time" class="col-sm-2 control-label">时长</label>
                <div class="col-sm-10">
                  <input v-model="section.time" type="text" class="form-control" id="time" placeholder="时长">
                </div>
              </div>
              <div class="form-group">
                <label for="charge" class="col-sm-2 control-label">收费</label>
                <div class="col-sm-10">
                  <select v-model="section.charge" class="form-control" id="charge">
                    <option v-for="o in SECTION_CHARGE" v-bind:value="o.key">{{ o.value }}</option>
                  </select>
                </div>
              </div>
              <div class="form-group">
                <label for="sort" class="col-sm-2 control-label">顺序</label>
                <div class="col-sm-10">
                  <input v-model="section.sort" type="text" class="form-control" id="sort" placeholder="顺序">
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
            <button v-on:click="save()" type="button" class="btn btn-primary">保存</button>
          </div>
        </div><!-- /.modal-content -->
      </div><!-- /.modal-dialog -->
    </div><!-- /.modal -->
  </div>

</template>

<script>
import Pagination from "../../components/pagination";
import File from "../../components/file";

export default {
  components: {Pagination, File},
  name: "business-section",
  data: function () {
    return {
      section: {}, // 用于绑定form表单的数据
      sections: [],
      SECTION_CHARGE: SECTION_CHARGE,
      FILE_USE: FILE_USE,
      course: {},
      chapter: {},
    }
  },
  mounted: function () {
    // sidebar激活样式方法一
    this.$parent.activeSidebar("business-course-sidebar");
    let _this = this;
    _this.$refs.pagination.size = 5;
    let course = SessionStorage.get(SESSION_KEY_COURSE) || {};
    let chapter = SessionStorage.get(SESSION_KEY_CHAPTER) || {};
    if (Tool.isEmpty(course) || Tool.isEmpty(chapter)) {
      _this.$router.push("/welcome");
    }
    _this.course = course;
    _this.chapter = chapter;
    _this.list(1);
  },
  methods: {
    /**
     * 点击编辑
     * @param section
     */
    edit(section) {
      let _this = this;
      _this.section = $.extend({}, section); // 列表数据复制一份赋值给表单section，防止修改表单时把列表也修改
      $("#form-modal").modal("show");//hide
    },
    /**
     * 点击新增
     */
    add() {
      let _this = this;
      //.modal里的modal是内置的方法,用于弹出或关闭模态框
      _this.section = {};
      $("#form-modal").modal("show");//hide
    },
    /**
     * 列表查询
     * @param page
     */
    list(page) {
      let _this = this;
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + "/business/admin/section/list", {
        page: page,
        size: _this.$refs.pagination.size,
        courseId: _this.course.id,
        chapterId: _this.chapter.id,
      }).then((response) => {
        Loading.hide();
        let resp = response.data;
        _this.sections = resp.content.list;
        _this.$refs.pagination.render(page, resp.content.total);
      });
    },
    /**
     * 点击保存
     */
    save() {
      let _this = this;

      // 保存校验
      if (1 != 1
          || !Validator.require(_this.section.title, "标题")
          || !Validator.length(_this.section.title, "标题", 1, 50)
          || !Validator.length(_this.section.video, "视频", 1, 200)
      ) {
        return;
      }
      _this.section.courseId = _this.course.id;
      _this.section.chapterId = _this.chapter.id;
      Loading.show();
      _this.$ajax.post(process.env.VUE_APP_SERVER + "/business/admin/section/save", _this.section).then((response) => {
        Loading.hide();
        let resp = response.data;
        if (resp.success) {
          $("#form-modal").modal("hide");
          _this.list(1);
          Toast.success("保存成功");
        } else {
          Toast.warning(resp.message);
        }
      });
    },
    /**
     * 点击删除
     * @param id
     */
    del(id) {
      let _this = this;
      Confirm.show("确认删除？", "删除后不可恢复，确认删除!", function () {
        _this.$ajax.delete(process.env.VUE_APP_SERVER + "/business/admin/section/delete/" + id).then((response) => {
          let resp = response.data;
          if (resp.success) {
            _this.list(1);
            Toast.success("删除成功");
          }
        })
      });
    },
    afterUpload(resp) {
      let _this = this;
      let video = resp.content.path;
      _this.section.video = video;
      _this.getTime();
    },
    /**
     * 获取时长
     */
    getTime() {
      let _this = this;
      let ele = document.getElementById("video");
      _this.section.time = parseInt(ele.duration, 10);
    }
  }
}
</script>

<style>
video {
  width: 100%;
  height: auto;
  margin-top: 10px;
}


</style>