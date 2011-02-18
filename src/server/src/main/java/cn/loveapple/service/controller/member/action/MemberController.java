package cn.loveapple.service.controller.member.action;

import java.util.Date;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.loveapple.service.controller.SessionLabel;
import cn.loveapple.service.controller.exception.ResourceNotFoundException;
import cn.loveapple.service.controller.member.form.MemberAuthForm;
import cn.loveapple.service.controller.member.form.MemberValidator;
import cn.loveapple.service.cool.model.LoveappleMemberModel;
import cn.loveapple.service.cool.service.MemberCoreService;

/**
 * サンプルコントローラ
 * 
 * @author $author:$
 * @version $Revision$
 * @date $Date$
 * @id $Id$
 *
 */
@Controller
@RequestMapping(value="/member")
public class MemberController implements SessionLabel{
	private MemberValidator validator = new MemberValidator();
	/**
	 * ログ
	 */
	private static Log log = LogFactory.getLog(MemberController.class);
		
	/**
	 * 会員情報操作ロジック
	 */
	private MemberCoreService memberCoreService;

	/**
	 * 登録入力
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "regist", method=RequestMethod.GET)
	public String regist(HttpSession session, Model model) {
		clearMemberInfo(session);
		MemberAuthForm form = new MemberAuthForm();
		
		model.addAttribute(form);
		return "member/regist";
	}
	
	/**
	 * 登録確認
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "registConfirm", method=RequestMethod.POST)
	public String registConfirm(HttpSession session, Model model) {
		clearMemberInfo(session);
		MemberAuthForm form = new MemberAuthForm();
		
		model.addAttribute(form);
		return "member/registConfirm";
	}
	
	/**
	 * 登録完了
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "registComplete", method=RequestMethod.POST)
	public String registComplete(HttpSession session, Model model) {
		clearMemberInfo(session);
		MemberAuthForm form = new MemberAuthForm();
		
		model.addAttribute(form);
		return "redirect:/member/core/info/" /*+ member.getKey().getId()*/;
	}

	/**
	 * 直打ちの場合、実行される
	 * 
	 * @param session
	 * @param model
	 * @return
	 */
	@RequestMapping(method=RequestMethod.GET)
	public String index(HttpSession session, Model model) {
		clearMemberInfo(session);
		MemberAuthForm form = new MemberAuthForm();
		
		model.addAttribute(form);
		return "member/index";
	}
	/**
	 * ポスト送信された場合、実行される
	 * 
	 * @param form
	 * @param result
	 * @return
	 */
	@RequestMapping(value="auth", method=RequestMethod.POST)
	public String auth(@Valid MemberAuthForm form, BindingResult result, HttpSession session) {
		if (result.hasErrors()) {
			return "member/index";
		}
		
		LoveappleMemberModel member = memberCoreService.authenticateLoveappleMember(form.getLoginId(), form.getPassword());

		if(member == null){
			if(log.isDebugEnabled()){
				log.debug("not have user:" + form.getLoginId());
			}
			result.reject("errors.auth");
			return "member/index";
		}
				
		session.setAttribute(LOVEAPPLE_MEMBER, member);
		
		Object refererUrl = String.valueOf(session.getAttribute(REFERER_INNER_URL));
		
		if(refererUrl != null){
			return "redirect:" + refererUrl;
		}
		return "redirect:/member/core/info/" + member.getKey().getId();
	}

	/**
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value="core/info/{id}", method=RequestMethod.GET)
	public String info(@PathVariable Long id, Model model) {
		
		LoveappleMemberModel member = memberCoreService.queryByKey(id);
		
		if(member == null){
			throw new ResourceNotFoundException(id);
		}
		
		model.addAttribute("member", member);
		
		return "member/core/info";
	}
	
	/**
	 * セッション情報から会員情報を削除する。
	 * 
	 * @param session セッション
	 */
	protected void clearMemberInfo(HttpSession session) {
		session.removeAttribute(LOVEAPPLE_MEMBER);
	}
	/**
	 * 会員情報操作ロジックを設定します。
	 * @param memberCoreService 会員情報操作ロジック
	 */
	@Autowired(required=true)
	public void setMemberCoreService(MemberCoreService memberCoreService) {
	    this.memberCoreService = memberCoreService;
	}
}