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
import sg.backend.dto.response.writefunding.ModifyContentResponseDto;
import sg.backend.dto.response.writefunding.reward.GetMRewardResponseDto;
import sg.backend.dto.response.writefunding.reward.GetPolicyResponseDto;
import sg.backend.dto.response.writefunding.reward.MakeRewardResponseDto;
import sg.backend.entity.Funding;
import sg.backend.entity.Reward;
import sg.backend.entity.State;
import sg.backend.repository.FundingRepository;
import sg.backend.repository.RewardRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FundingRewardService {

    private final FundingRepository fundingRepository;
    private final RewardRepository rewardRepository;

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
    public ResponseEntity<? super ModifyContentResponseDto> insertRewardInfo(Long funding_id, PolicyRewardRequestDto dto){
        try{
            option = fundingRepository.findById(funding_id);
            if(option.isEmpty()) {
                return ModifyContentResponseDto.not_existed_post();
            }
            Funding funding = option.get();

            funding.setRewardInfo(dto.getReward_info());
        } catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return ModifyContentResponseDto.success();
    }

    @Transactional
    public ResponseEntity<? super ModifyContentResponseDto> insertRefundPolicy(Long funding_id, PolicyRefundRequestDto dto){
        try{
            option = fundingRepository.findById(funding_id);
            if(option.isEmpty()) {
                return ModifyContentResponseDto.not_existed_post();
            }
            Funding funding = option.get();

            funding.setRefundPolicy(dto.getRefund_policy());
        } catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return ModifyContentResponseDto.success();
    }

    @Transactional
    public ResponseEntity<? super ModifyContentResponseDto> submit_funding(Long funding_id){
        try{
            option = fundingRepository.findById(funding_id);
            if(option.isEmpty()) {
                return ModifyContentResponseDto.not_existed_post();
            }
            Funding funding = option.get();

            State current = State.REVIEW;
            funding.setCurrent(current);

        } catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return ModifyContentResponseDto.success();
    }

    @Transactional
    public ResponseEntity<? super ModifyContentResponseDto> giveup_funding(Long funding_id){
        try{
            option = fundingRepository.findById(funding_id);
            if(option.isEmpty()) {
                return ModifyContentResponseDto.not_existed_post();
            }
            Funding funding = option.get();

            fundingRepository.delete(funding);
        } catch(Exception e){
            e.printStackTrace();
            return ResponseDto.databaseError();
        }
        return ModifyContentResponseDto.success();
    }
}
