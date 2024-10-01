package sg.backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import sg.backend.dto.request.wirtefunding.MakeRewardRequestDto;
import sg.backend.dto.request.wirtefunding.PolicyRefundRequestDto;
import sg.backend.dto.request.wirtefunding.PolicyRewardRequestDto;
import sg.backend.dto.response.*;
import sg.backend.dto.response.writefunding.DeleteDataResponseDto;
import sg.backend.dto.response.writefunding.SubmitFundingResponseDto;
import sg.backend.dto.response.writefunding.file.DeleteFileResponseDto;
import sg.backend.dto.response.writefunding.reward.GetMRewardResponseDto;
import sg.backend.dto.response.writefunding.reward.GetPolicyResponseDto;
import sg.backend.dto.response.writefunding.reward.MakeRewardResponseDto;
import sg.backend.entity.*;
import sg.backend.repository.DocumentRepository;
import sg.backend.repository.FundingRepository;
import sg.backend.repository.IntroImageRepository;
import sg.backend.repository.RewardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FundingRewardService {

    private final FundingRepository fundingRepository;
    private final RewardRepository rewardRepository;
    private final DocumentRepository documentRepository;
    private final IntroImageRepository introImageRepository;

    private final FundingFileService fileService;
    private Optional<Funding> option;

    @Transactional
    public ResponseEntity<? super GetMRewardResponseDto> getReward(Long funding_id){
        String[] amount;
        String[] name;
        String[] description;
        String[] quantity;
        Long[] reward_id;
        try{
            option = fundingRepository.findById(funding_id);
            if(option.isEmpty()) {
                return MakeRewardResponseDto.not_existed_post();
            }
            Funding funding = option.get();

            List<Reward> rewardList = rewardRepository.findAllByFunding(funding);
            if(rewardList.isEmpty()){
                amount = new String[0];
                name = new String[0];
                description = new String[0];
                quantity = new String[0];
                reward_id = null;
            } else{
                ArrayList<String> amounts = new ArrayList<>();
                ArrayList<String> names = new ArrayList<>();
                ArrayList<String> descriptions = new ArrayList<>();
                ArrayList<String> quantitys = new ArrayList<>();
                ArrayList<Long> ids = new ArrayList<>();
                for(Reward reward: rewardList){
                    amounts.add(reward.getAmount().toString());
                    names.add(reward.getRewardName());
                    descriptions.add(reward.getRewardDescription());
                    quantitys.add(reward.getQuantity().toString());
                    ids.add(reward.getRewardId());
                }
                amount = amounts.toArray(new String[0]);
                name = names.toArray(new String[0]);
                description = descriptions.toArray(new String[0]);
                quantity = quantitys.toArray(new String[0]);
                reward_id = ids.toArray(new Long[0]);
            }
        } catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetMRewardResponseDto.success(amount, name, description,quantity, reward_id);
    }

    @Transactional
    public ResponseEntity<? super MakeRewardResponseDto> insertReward(Long funding_id, MakeRewardRequestDto dto){
        Long reward_id;
        try{
            if(dto.getAmount()==null || dto.getReward_name()==null || dto.getReward_description()==null || dto.getQuantity()==null){
                return MakeRewardResponseDto.not_existed_data();
            }

            Integer amount = Integer.parseInt(dto.getAmount());
            String reward_name = dto.getReward_name();
            String reward_description = dto.getReward_description();
            Integer quantity = Integer.parseInt(dto.getQuantity());

            option = fundingRepository.findById(funding_id);
            if(option.isEmpty()) {
                return MakeRewardResponseDto.not_existed_post();
            }
            Funding funding = option.get();

            Reward reward = new Reward(funding, amount, reward_name,reward_description,quantity);
            rewardRepository.save(reward);
            reward_id = reward.getRewardId();
        }catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return MakeRewardResponseDto.success(reward_id);
    }

    @Transactional
    public ResponseEntity<? super DeleteDataResponseDto> deleteReward(Long reward_id){
        try{
            rewardRepository.deleteById(reward_id);
        } catch(Exception e){
            e.printStackTrace();
            return DeleteDataResponseDto.not_existed_data();
        }
        return DeleteDataResponseDto.success();
    }

    @Transactional
    public ResponseEntity<? super GetPolicyResponseDto> getPolicy(Long funding_id){
        String refund_policy;
        String reward_info;
        try{
            option = fundingRepository.findById(funding_id);
            if(option.isEmpty()) {
                return GetPolicyResponseDto.not_existed_post();
            }
            Funding funding = option.get();
            refund_policy = funding.getRefundPolicy();
            reward_info = funding.getRewardInfo();
        } catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return GetPolicyResponseDto.success(refund_policy,reward_info);
    }

    @Transactional
    public ResponseEntity<ResponseDto> insertRewardInfo(Long funding_id, PolicyRewardRequestDto dto){
        try{
            option = fundingRepository.findById(funding_id);
            if(option.isEmpty()) {
                return ResponseDto.noExistFunding();
            }
            Funding funding = option.get();

            funding.setRewardInfo(dto.getReward_info());
        } catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return ResponseDto.success();
    }

    @Transactional
    public ResponseEntity<ResponseDto> insertRefundPolicy(Long funding_id, PolicyRefundRequestDto dto){
        try{
            option = fundingRepository.findById(funding_id);
            if(option.isEmpty()) {
                return ResponseDto.noExistFunding();
            }
            Funding funding = option.get();

            funding.setRefundPolicy(dto.getRefund_policy());
        } catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return ResponseDto.success();
    }

    @Transactional
    public ResponseEntity<? super SubmitFundingResponseDto> submit_funding(Long funding_id){
        try{
            option = fundingRepository.findById(funding_id);
            if(option.isEmpty()) {
                return ResponseDto.noExistFunding();
            }
            Funding funding = option.get();

            //프로젝트 정보 작성에서 빠진 내용
            if(funding.getCategory() == Category.NONE){
                return SubmitFundingResponseDto.not_existed_info();
            }
            if(funding.getOrganizerIdCard() == null || funding.getOrganizerIdCard().isEmpty()){
                return SubmitFundingResponseDto.not_existed_info();
            }
            if(funding.getOrganizerEmail() == null || funding.getOrganizerEmail().isEmpty()){
                return SubmitFundingResponseDto.not_existed_info();
            }
            if(funding.getTaxEmail() == null || funding.getTaxEmail().isEmpty()){
                return SubmitFundingResponseDto.not_existed_info();
            }
            if(funding.getStartDate() == null){
                return SubmitFundingResponseDto.not_existed_info();
            }
            if(funding.getEndDate() == null){
                return SubmitFundingResponseDto.not_existed_info();
            }
            if(funding.getTargetAmount() == null){
                return SubmitFundingResponseDto.not_existed_info();
            }

            //프로젝트 스토리에서 빠진 내용
            if(funding.getMainImage() == null || funding.getMainImage().isEmpty()){
                return SubmitFundingResponseDto.not_existed_story();
            }
            if(funding.getTitle() == null || funding.getTitle().isEmpty()){
                return SubmitFundingResponseDto.not_existed_story();
            }
            //if(funding.getStory() == null || funding.getStory().isEmpty()){
            //    return SubmitFundingResponseDto.not_existed_story();
            //}

            //리워드가 적어도 하나
            List<Reward> rewardList = rewardRepository.findAllByFunding(funding);
            if(rewardList.isEmpty()){
                return SubmitFundingResponseDto.not_existed_reward();
            }

            //정책 정보가 없는 경우
            if(funding.getRefundPolicy() == null || funding.getRefundPolicy().isEmpty()){
                return SubmitFundingResponseDto.not_existed_policy();
            }
            if(funding.getRewardInfo() == null || funding.getRewardInfo().isEmpty()){
                return SubmitFundingResponseDto.not_existed_policy();
            }

            State current = State.REVIEW;
            funding.setCurrent(current);

        } catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return ResponseDto.success();
    }

    @Transactional
    public ResponseEntity<? super DeleteFileResponseDto> giveup_funding(Long funding_id){
        try{
            option = fundingRepository.findById(funding_id);
            if(option.isEmpty()) {
                return DeleteFileResponseDto.not_existed_post();
            }
            Funding funding = option.get();

            //id_card가 안 비워져 있으면 삭제
            if(funding.getOrganizerIdCard() != null){
                if(!fileService.file_delete(funding.getOrganizerIdCard())){
                    return ResponseDto.databaseError();
                }
            }
            //main image 삭제
            if(funding.getMainImage() != null){
                if(!fileService.file_delete(funding.getMainImage())){
                    return ResponseDto.databaseError();
                }
            }

            // 파일 다 삭제(documentlist, introimagelist, storyfilelist)
            List<Document> documentList = documentRepository.findAllByFunding(funding);
            for(Document docu : documentList){
                if(docu == null || docu.getFpath() == null || docu.getFpath().isEmpty()){
                    return DeleteFileResponseDto.not_existed_file();
                }
                if(!fileService.file_delete(docu.getFpath())){
                    return ResponseDto.databaseError();
                }
            }
            List<IntroImage> imageList = introImageRepository.findAllByFunding(funding);
            for(IntroImage image : imageList){
                if(image == null || image.getFpath() == null || image.getFpath().isEmpty()){
                    return DeleteFileResponseDto.not_existed_file();
                }
                if(!fileService.file_delete(image.getFpath())){
                    return ResponseDto.databaseError();
                }
            }
            //storyfileList


            fundingRepository.delete(funding);
        } catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return DeleteFileResponseDto.success();
    }
}
