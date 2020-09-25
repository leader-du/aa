package com.ssvet.approval.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.ssvet.approval.entity.*;
import com.ssvet.approval.mapper.*;
import com.ssvet.approval.service.IEventService;
import com.ssvet.approval.utils.exception.ApprovalException;
import com.ssvet.approval.utils.resp.CommonResult;
import com.ssvet.approval.utils.upload.GetPath;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 审批事件表 服务实现类
 * </p>
 *
 * @author 刘志红
 * @since 2020-08-25
 */
@Service
@Slf4j
public class EventServiceImpl implements IEventService {
    @Autowired
    private EventMapper eventMapper;
    @Autowired
    private NoteMapper noteMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private DeptMapper deptMapper;

    @Transactional
    @Override
    public CommonResult getApprovalList(User user) {
        //获取需要用户审批的申请
//        List<Event> list = eventMapper.selectList(new QueryWrapper<Event>().eq("now_approval_id", user.getId()).notIn("status",3,4));
        List<Event> list = eventMapper.selectList(new QueryWrapper<Event>().eq("now_approval_id", user.getId()));

        list = this.addAttribute(list);

//        if(list.size() != 0){

            return CommonResult.success(list);
//        }

//        return CommonResult.success(0);

    }

    @Override
    @Transactional
    public CommonResult getApprovalList(Integer id) {

       //查询用户所有的审批
        List<Event> list = eventMapper.selectList(new QueryWrapper<Event>().eq("approval_user_id", id));

        list = this.addAttribute(list);

        return CommonResult.success(list);
    }

    @Override
    @Transactional
    public CommonResult addApproval(Event e) {

        CommonResult commonResult = parameterValidate(e);
        if (commonResult != null) {
            return commonResult;
        }
        e.setApprovalUserIndex(0);
        String[] users = e.getApprovalPersons().split(",");
        e.setNowApprovalId(Integer.valueOf(users[e.getApprovalUserIndex()]));
        e.setCreateDate(LocalDate.now());
        //e.setStatus(1);
        eventMapper.insert(e);
        //图片信息
        List<Note> notes = e.getNotes();

        if (!StringUtils.isEmpty(notes) && notes.size() > 0) {

            for (int i = 0; i < notes.size(); i++) {

                notes.get(i).setNoteEventId(e.getApprovalEventId());

                noteMapper.insert(notes.get(i));
            }
        }
        return CommonResult.success(e,"发起审批成功");
    }

    @Override
    @Transactional
    public CommonResult updateApproval(Event e) {
        CommonResult commonResult = parameterValidate(e);
        if (commonResult != null) {
            return commonResult;
        }
        if (StringUtils.isEmpty(e.getApprovalEventId())) {
            log.info("修改审批参数有误");
            return CommonResult.validateFailed("服务器错误，请联系管理员");
        }
        Event event = eventMapper.selectById(e.getApprovalEventId());
        e.setVersion(event.getVersion());
        e.setApprovalUserIndex(0);
        e.setNowApprovalId(Integer.valueOf(e.getApprovalPersons().split(",")[0]));
        eventMapper.updateById(e);
        // 更新图片之前先删除事件关联的所有图片

        noteMapper.delete(new QueryWrapper<Note>().eq("note_event_id",event.getApprovalEventId()));

        //图片信息
        List<Note> notes = e.getNotes();

        if (!StringUtils.isEmpty(notes) && notes.size() > 0) {

            for (int i = 0; i < notes.size(); i++) {

                notes.get(i).setNoteEventId(e.getApprovalEventId());

                noteMapper.insert(notes.get(i));
            }
        }

        //删除审批记录
        recordMapper.delete(new QueryWrapper<Record>().eq("event_id",e.getApprovalEventId()));

        return CommonResult.success("更新成功");
    }

    @Transactional
    @Override
    public CommonResult saveApproval(Event event, MultipartRequest request) {
        CommonResult commonResult = parameterValidate(event);
        if (commonResult != null) {
            return commonResult;
        }
        eventMapper.insert(event);
        //插入数据后获取主键
        log.info("event = " + event.getApprovalEventId());
        //处理上传
        String path = GetPath.getUploadPath("static/approvalImg/");
        Map<String, MultipartFile> map = request.getFileMap();
        List<Note> notes = new ArrayList<>();
        for (String s : map.keySet()) {

            MultipartFile imgFile = map.get(s);

            String oldName = imgFile.getOriginalFilename();
            if (oldName == null || oldName.length() <= 0) {
                throw new ApprovalException(401, "上传文件出错");
            }
            if (oldName.matches("^\\w+(.jpg|.gif|.ico|.jpeg|.tif|.png)$")){
                throw new ApprovalException(401, "上传图片格式有误");
            }

            String uuid = UUID.randomUUID().toString().substring(2, 20).replace("-", "");

            String name = uuid + "-" + oldName;

            String url = path + File.separator + name;

            File file = new File(url);

            try {

                imgFile.transferTo(file);

                Note note = new Note();

                note.setApprovalNoteUrl("approvalImg/" + name);

                note.setNoteEventId(event.getApprovalEventId());

                noteMapper.insert(note);
                notes.add(note);
            } catch (IOException e) {

                throw new RuntimeException("上传失败");

            }


        }
        event.setNotes(notes);
        return CommonResult.success(event, "提交审批成功");
    }

    @Override
    public List<Event> getCompletedApprovalList(Integer days) {

        List<Event> list = eventMapper.getCompletedApprovalList(days);

        list = this.addAttribute(list);

        for (Event event : list) {

            List<Record> records = recordMapper.selectList(new QueryWrapper<Record>().eq("event_id",event.getApprovalEventId()).orderByAsc("approval_date"));

            event.setRecords(records);
        }


        return list;
    }

    private CommonResult parameterValidate(Event e) {
        CommonResult commonResult = null;
        if (StringUtils.isEmpty(e)) {
            log.error("参数为空");
            return CommonResult.validateFailed("服务器错误，请联系管理员");
        } else if (StringUtils.isEmpty(e.getApprovalTypeId())) {
            log.error("参数错误");
            CommonResult.validateFailed("审批类型不能为空");
        } else if (StringUtils.isEmpty(e.getApprovalUserId())) {
            //todo 添加认证后修改
            log.error("申请人id为空");
            CommonResult.validateFailed("申请人id不能为空");
        } else if (StringUtils.isEmpty(e.getApprovalMoney())) {
            CommonResult.failed("审批金额不能为空");
        } else if (StringUtils.isEmpty(e.getApprovalRemark())) {
            CommonResult.failed("审批备注不能为空");
        } else if (StringUtils.isEmpty(e.getApprovalPersons())) {
            CommonResult.failed("审批人名单不能为空");
        }
        return commonResult;
    }

    private List<Event> addAttribute(List<Event> list){

        for (Event event : list) {

            List<Note> notes = noteMapper.selectList(new QueryWrapper<Note>().eq("note_event_id", event.getApprovalEventId()));

            event.setNotes(notes);

            String persons = event.getApprovalPersons();

            String[] persons_id = persons.split(",");

            // 必须保证审批顺序，如果用in顺序会乱，所以单独处理

            //List<User> users = userMapper.selectList(new QueryWrapper<User>().in("uid", persons_id));

            List<User> users = new ArrayList<>();

            for (String s : persons_id) {

                users.add(userMapper.selectById(s));

            }

            event.setUsers(users);

            User u = userMapper.selectById(event.getApprovalUserId());

            event.setChName(u.getChName());

            //设置发起审批的人的部门信息

            Dept dept = deptMapper.selectById(u.getDeptId());

            u.setDept(dept);

            event.setUser(u);


        }

        return list;
    }
}
